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

import scala.reflect.runtime.{universe => ru}

import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS
import io.deepsense.deeplang.doperables.machinelearning.logisticregression.{LogisticRegressionParameters, UntrainedLogisticRegression}
import io.deepsense.deeplang.parameters.{NumericParameter, ParametersSchema, RangeValidator}
import io.deepsense.deeplang.{DOperation, DOperation0To1, ExecutionContext}

case class CreateLogisticRegression() extends DOperation0To1[UntrainedLogisticRegression] {
  @transient
  override lazy val tTagTO_0: ru.TypeTag[UntrainedLogisticRegression] =
    ru.typeTag[UntrainedLogisticRegression]

  import CreateLogisticRegression._
  override val name = "Create Logistic Regression"

  override val id: DOperation.Id = "ed20e602-ff91-11e4-a322-1697f925ec7b"

  override val parameters = ParametersSchema(
    Regularization -> NumericParameter(
      description = "Regularization parameter",
      default = Some(0.0),
      validator = RangeValidator(begin = 0.0, end = Double.PositiveInfinity)),
    IterationsNumberKey -> NumericParameter(
      description = "Max number of iterations to perform",
      default = Some(10.0),
      validator = RangeValidator(begin = 1.0, end = EndOfRange, step = Some(1.0))),
    Tolerance -> NumericParameter(
      description = "The convergence tolerance of iterations for LBFGS. " +
        "Smaller value will lead to higher accuracy at a cost of more iterations.",
      default = Some(0.0001),
      validator = RangeValidator(begin = 0.0, end = EndOfRange, beginIncluded = false)))

  override protected def _execute(context: ExecutionContext)(): UntrainedLogisticRegression = {
    val regParam = parameters.getDouble(Regularization)
    val iterationsParam = parameters.getDouble(IterationsNumberKey)
    val toleranceParam = parameters.getDouble(Tolerance)

    def createModelInstance(): LogisticRegressionWithLBFGS = {
      val model = new LogisticRegressionWithLBFGS
      model
        .setIntercept(true)
        .setNumClasses(2)
        .setValidateData(false)
        .optimizer
        .setRegParam(regParam)
        .setNumIterations(iterationsParam.toInt)
        .setConvergenceTol(toleranceParam)

      model
    }

    val modelParameters = LogisticRegressionParameters(regParam, iterationsParam, toleranceParam)
    // We're passing a factory method here, instead of constructed object,
    // because the resulting UntrainedLogisticRegression could be used multiple times
    // in a workflow and its underlying Spark model is mutable
    UntrainedLogisticRegression(modelParameters, createModelInstance)
  }
}

object CreateLogisticRegression {
  val Regularization = "regularization"
  val IterationsNumberKey = "iterations number"
  val Tolerance = "tolerance"
  val EndOfRange = 1000000.0

  def apply(regularization: Double, numberOfIterations: Int, tolerance: Double)
      : CreateLogisticRegression = {
    val createLogisticRegression: CreateLogisticRegression = CreateLogisticRegression()
    createLogisticRegression.parameters.getNumericParameter(
      CreateLogisticRegression.Regularization).value = regularization
    createLogisticRegression.parameters.getNumericParameter(
      CreateLogisticRegression.IterationsNumberKey).value = numberOfIterations
    createLogisticRegression.parameters.getNumericParameter(
      CreateLogisticRegression.Tolerance).value = tolerance
    createLogisticRegression
  }
}
