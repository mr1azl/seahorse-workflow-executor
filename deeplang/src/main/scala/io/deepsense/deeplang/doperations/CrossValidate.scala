/**
 * Copyright 2015, deepsense.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.deepsense.deeplang.doperations

import java.util.UUID

import scala.reflect.runtime.{universe => ru}

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.expressions.GenericRow

import io.deepsense.deeplang.doperables._
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperations.exceptions.InvalidDataFrameException
import io.deepsense.deeplang.inference.{InferContext, InferenceWarnings}
import io.deepsense.deeplang.parameters.ChoiceParameter.BinaryChoice
import io.deepsense.deeplang.parameters.{ChoiceParameter, NumericParameter, ParametersSchema, RangeValidator}
import io.deepsense.deeplang.{DKnowledge, DOperation2To2, ExecutionContext}
import io.deepsense.reportlib.model.ReportContent

abstract class CrossValidate[T <: Evaluable]()
  extends DOperation2To2[
    Trainable with T, DataFrame,
    Scorable with T, Report]
  with CrossValidateParams
  with TrainableParameters {

  def reportName: String

  override val parameters = trainParameters ++ ParametersSchema(
    "number of folds" -> numberOfFoldsParameter,
    "shuffle" -> shuffleParameter
  )

  override protected val dataFramePortIndex: Int = 1

  override protected def _execute(
      context: ExecutionContext)(
      trainable: Trainable with T,
      dataFrame: DataFrame): (Scorable with T, Report) = {

    logger.debug("Execution of CrossValidator starts")

    val dataFrameSize = dataFrame.sparkDataFrame.count()

    val effectiveNumberOfFolds = (dataFrameSize, numberOfFoldsParameter.value) match {
      case (0, _) => throw InvalidDataFrameException("cannot train a model on an empty DataFrame")
      // If either dataFrameSize or numberOfFolds is 1, no folds can be performed
      case (1, _) => 0
      case (_, 1) => 0
      // Now, the number of folds is at least 2
      case (dfs, nof) => math.min(dfs, math.round(nof)).toInt
    }

    val seed = BinaryChoice.withName(shuffleParameter.value) match {
      case BinaryChoice.NO => 0
      case BinaryChoice.YES => math.round(seedShuffleParameter.value)
    }

    logger.debug(
      s"Effective number of folds = $effectiveNumberOfFolds, DataFrame size = $dataFrameSize")

    // TODO: (DS-546) Do not use sample method, perform shuffling "in flight"
    // during splitting DataFrame on training and test DataFrame.
    val shuffledDataFrame = BinaryChoice.withName(shuffleParameter.value) match {
      case BinaryChoice.NO => dataFrame
      case BinaryChoice.YES =>
        context.dataFrameBuilder.buildDataFrame(
          dataFrame.sparkDataFrame.sample(withReplacement = false, 1.0, seed))
    }

    // Number of folds == 0 means that cross-validation report is not needed
    val report =
      if (effectiveNumberOfFolds > 0) {
        generateCrossValidationReport(context, trainable, shuffledDataFrame, effectiveNumberOfFolds)
      } else {
        Report(ReportContent(reportName))
      }

    logger.debug("Train model on all available data")
    val scorable = trainable.train(context)(this)(dataFrame)

    logger.debug("Execution of CrossValidator ends")
    (scorable.asInstanceOf[T with Scorable], report)
  }

  def generateCrossValidationReport(
      context: ExecutionContext,
      trainable: Trainable with T,
      dataFrame: DataFrame,
      numberOfFolds: Int): Report = {

    val schema = dataFrame.sparkDataFrame.schema
    val rddWithIndex: RDD[(Row, Long)] = dataFrame.sparkDataFrame.rdd.map(
      r => new GenericRow(r.toSeq.toArray).asInstanceOf[Row]).zipWithIndex().cache()

    val reporter : Reporter = trainable.asInstanceOf[T].getReporter

    def createFold(splitIndex: Int): Reporter.CrossValidationFold = {
      logger.debug("Preparing cross-validation report: split index [0..N-1]=" + splitIndex)
      val training = rddWithIndex
        .filter { case (r, index) => index % numberOfFolds != splitIndex }
        .map { case (r, index) => r }
      val test = rddWithIndex
        .filter { case (r, index) => index % numberOfFolds == splitIndex }
        .map { case (r, index) => r }

      val trainingDataFrame = context.dataFrameBuilder.buildDataFrame(
        context.sqlContext.createDataFrame(training, schema))
      val testDataFrame = context.dataFrameBuilder.buildDataFrame(
        context.sqlContext.createDataFrame(test, schema))

      // Train model on trainingDataFrame
      val trained: Scorable = trainable.train(context)(this)(trainingDataFrame)

      // Score model on trainingDataFrame (with random column name for predictions)
      val predictionColumnName = UUID.randomUUID().toString
      val scoredDataFrame = trained.score(context)(predictionColumnName)(testDataFrame)

      val observationColumnName =
        testDataFrame.getColumnName(targetColumnParameter.value)

      val predictionsAndLabels =
        scoredDataFrame.sparkDataFrame
          .select(predictionColumnName, observationColumnName).rdd.map(
              r => (r.getDouble(0), r.getDouble(1)))

      Reporter.CrossValidationFold(
        trainingDataFrame.sparkDataFrame.count(),
        testDataFrame.sparkDataFrame.count(),
        predictionsAndLabels)
    }

    val report = reporter.crossValidationReport(numberOfFolds, createFold)

    rddWithIndex.unpersist()

    report
  }

  override protected def _inferKnowledge(context: InferContext)(
      trainableKnowledge: DKnowledge[Trainable with T],
      dataFrameKnowledge: DKnowledge[DataFrame]
      ): ((DKnowledge[T with Scorable], DKnowledge[Report]), InferenceWarnings) = {

    val scorableKnowledge = for {
      trainable <- trainableKnowledge.types
      (result, _) = trainable.train.infer(context)(this)(dataFrameKnowledge)
    } yield result.asInstanceOf[DKnowledge[T with Scorable]]

    ((DKnowledge(scorableKnowledge), DKnowledge(Report())), InferenceWarnings.empty)
  }

  @transient
  override lazy val tTagTO_1: ru.TypeTag[Report] = ru.typeTag[Report]
  @transient
  override lazy val tTagTI_1: ru.TypeTag[DataFrame] = ru.typeTag[DataFrame]
}

trait CrossValidateParams {
  val numberOfFoldsParameter = NumericParameter(
    "How many folds should be used in cross-validation?",
    Some(10.0),
    validator = RangeValidator(
      0, Int.MaxValue / 2, beginIncluded = true, endIncluded = true, Some(1.0)),
    _value = None)

  val seedShuffleParameter = NumericParameter(
    "Seed for random generator used during shuffling",
    default = Some(0.0),
    validator = RangeValidator(
      Int.MinValue / 2, Int.MaxValue / 2, beginIncluded = true, endIncluded = true, Some(1.0)),
    _value = None)

  val shuffleParameter = ChoiceParameter.binaryChoice(
    "Should the DataFrame be shuffled before cross-validation?",
    Some(BinaryChoice.YES.toString),
    yesSchema = ParametersSchema("seed" -> seedShuffleParameter),
    noSchema = ParametersSchema()
  )
}
