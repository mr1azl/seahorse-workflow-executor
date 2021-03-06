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

import org.apache.spark.rdd.RDD

import io.deepsense.deeplang.DOperation.Id
import io.deepsense.deeplang.doperables.{ColumnTypesPredicates, SupervisedEvaluator, RegressionReporter, Report}
import io.deepsense.deeplang.parameters.NameSingleColumnSelection

case class EvaluateRegression() extends SupervisedEvaluator {

  override val name: String = "Evaluate Regression"

  override val id: Id = "f2a43e21-331e-42d3-8c02-7db1da20bc00"

  override protected def report(predictionsAndLabels: RDD[(Double, Double)]): Report =
    RegressionReporter.report(predictionsAndLabels)

  override protected def validateTarget: ColumnTypesPredicates.Predicate =
    ColumnTypesPredicates.isNumeric
}

object EvaluateRegression {
  def apply(
      targetColumnName: String,
      predictionColumnName: String): EvaluateRegression = {
    val operation = EvaluateRegression()

    operation.targetColumnParameter.value =
      NameSingleColumnSelection(targetColumnName)
    operation.predictionColumnParameter.value =
      NameSingleColumnSelection(predictionColumnName)

    operation
  }
}
