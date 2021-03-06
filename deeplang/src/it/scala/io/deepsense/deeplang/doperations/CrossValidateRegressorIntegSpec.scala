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

import org.apache.spark.sql.Row
import org.apache.spark.sql.types._
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{BeforeAndAfter, Matchers}

import io.deepsense.commons.utils.Logging
import io.deepsense.deeplang._
import io.deepsense.deeplang.doperables._
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.doperables.machinelearning.ridgeregression.TrainedRidgeRegression
import io.deepsense.deeplang.doperations.exceptions.InvalidDataFrameException
import io.deepsense.deeplang.parameters.ChoiceParameter.BinaryChoice
import io.deepsense.deeplang.parameters.{MultipleColumnSelection, NameColumnSelection, NameSingleColumnSelection}

class CrossValidateRegressorIntegSpec
  extends DeeplangIntegTestSupport
  with GeneratorDrivenPropertyChecks
  with Matchers
  with Logging
  with BeforeAndAfter {

  before {
    createDir("target/tests/model")
  }

  // Example training set
  val schema = StructType(List(
    StructField("column1", StringType),
    StructField("column2", DoubleType),
    StructField("column3", DoubleType)))
  val rowsSeq: Seq[Row] = Seq(
    Row("a", 1.0, 1.0),
    Row("b", 2.0, 2.0),
    Row("c", 3.0, 3.0),
    Row("d", 1.0, 1.0),
    Row("e", 2.0, 2.0),
    Row("f", 3.0, 3.0),
    Row("g", 1.0, 1.0),
    Row("h", 2.0, 2.0),
    Row("i", 3.0, 3.0),
    Row("j", 1.0, 1.0),
    Row("k", 2.0, 2.0),
    Row("l", 3.0, 3.0),
    Row("m", 1.0, 1.0),
    Row("n", 2.0, 2.0),
    Row("o", 3.0, 3.0),
    Row("p", 1.0, 1.0),
    Row("r", 2.0, 2.0),
    Row("s", 3.0, 3.0),
    Row("t", 1.0, 1.0),
    Row("u", 2.0, 2.0),
    Row("w", 3.0, 3.0),
    Row("x", 1.0, 1.0),
    Row("y", 2.0, 2.0),
    Row("z", 3.0, 3.0),
    Row("aa", 1.0, 1.0))

  val numberOfFolds = 4
  val regressor = new CrossValidateRegressor
  regressor.numberOfFoldsParameter.value = numberOfFolds * 1.0
  regressor.shuffleParameter.value = BinaryChoice.YES.toString
  regressor.seedShuffleParameter.value = 0.0

  regressor.targetColumnParameter.value =
    NameSingleColumnSelection("column3")
  regressor.featureColumnsParameter.value =
    MultipleColumnSelection(Vector(NameColumnSelection(Set("column2"))), excluding = false)

  "CrossValidateRegressor with parameters set" should {
    "train untrained model on DataFrame and produce report" in {
      val dataFrame = createDataFrame(rowsSeq, schema)
      testCrossValidateRegressor(regressor, dataFrame)
    }

    "train untrained model on DataFrame with size 1 and do not produce report" in {
      val dataFrame = createDataFrame(Seq(rowsSeq.head), schema)
      testCrossValidateRegressor(regressor, dataFrame)
    }

    "train untrained model on DataFrame with size equal to number of folds" +
      " and produce report (leave-one-out)" in {
      val dataFrame = createDataFrame(rowsSeq.slice(0, numberOfFolds), schema)
      testCrossValidateRegressor(regressor, dataFrame)
    }

    "fail when training DataFrame has size 0" in {
      val dataFrame = createDataFrame(Seq(), schema)
      intercept[InvalidDataFrameException] {
        testCrossValidateRegressor(regressor, dataFrame)
      }
      ()
    }
  }

  private def testCrossValidateRegressor(
      regressor: CrossValidateRegressor,
      dataFrame: DataFrame): Unit = {
    val effectiveNumberOfFolds = math.min(
      if (dataFrame.sparkDataFrame.count() == 1) 0 else dataFrame.sparkDataFrame.count(),
      math.round(regressor.numberOfFoldsParameter.value).toInt)

    // Training untrained RidgeReggressor
    val createRidgeRegression = CreateRidgeRegression(0.0, 10)
    val ridgeRegression = createRidgeRegression.execute(executionContext)(Vector()).head
    val result = regressor.execute(executionContext)(Vector(ridgeRegression, dataFrame))

    // Check result value
    result.head.isInstanceOf[TrainedRidgeRegression] shouldBe true

    val report = result.last.asInstanceOf[Report]
    val content = report.content
    //logger.debug("Cross-validation report=" + content.toJson.prettyPrint)

    if (effectiveNumberOfFolds > 0) {
      val table = content.tables.get("Cross-validate Regression Report").get

      // Check number of rows in report (one for every fold + one for summary)
      table.rowNames.get.length shouldBe (effectiveNumberOfFolds + 1)
      table.values.length shouldBe table.rowNames.get.length

      // Check number of columns in report
      table.columnNames.get shouldBe RegressionReporter.CvReportColumnNames
      table.values.foreach(r => r.length shouldBe table.columnNames.get.length)

      // Check sizes of test sets
      val allTestSetsSize = table.values.slice(0, effectiveNumberOfFolds.toInt).fold(0) {
        case (z: Int, _ :: Some(trainingSetSize: String) :: Some(testSetSize: String) :: _) =>
          trainingSetSize.toInt + testSetSize.toInt shouldBe dataFrame.sparkDataFrame.count()
          z + testSetSize.toInt
      }
      // Sum of test sets sizes from all folds should be equal to size of DataFrame
      allTestSetsSize shouldBe dataFrame.sparkDataFrame.count()

      val summaryRow = table.values.last
      summaryRow.head shouldBe Some("average")
    }
  }
}
