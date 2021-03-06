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

import io.deepsense.deeplang.DOperation._
import io.deepsense.deeplang.doperables.transformations.MathematicalTransformation
import io.deepsense.deeplang.parameters.{AcceptAllRegexValidator, ParametersSchema, StringParameter}
import io.deepsense.deeplang.{DOperation0To1, ExecutionContext}

case class CreateMathematicalTransformation() extends DOperation0To1[MathematicalTransformation] {

  override val name : String = "Create Mathematical Transformation"

  override val id : Id = "ecb9bc36-5f7c-4a62-aa18-8db6e2d73251"

  // TODO: DS-635 This operation will fail if user provide column name with '.'

  override protected def _execute(context: ExecutionContext)(): MathematicalTransformation = {
    val formula = formulaParam.value
    val columnName = columnNameParam.value
    MathematicalTransformation(formula, columnName)
  }

  val formulaParam = StringParameter(
    description = "Mathematical formula. For example, \"(myColumn * myColumn)\"",
    default = None,
    validator = new AcceptAllRegexValidator)

  val columnNameParam = StringParameter(
    description = "Name of the newly created column holding the result.",
    default = None,
    validator = new AcceptAllRegexValidator)

  override val parameters = ParametersSchema(
    "formula" -> formulaParam,
    "column name" -> columnNameParam
  )

  @transient
  override lazy val tTagTO_0: ru.TypeTag[MathematicalTransformation] =
    ru.typeTag[MathematicalTransformation]
}

object CreateMathematicalTransformation {
  def apply(formula: String, columnName: String): CreateMathematicalTransformation = {

    val operation = new CreateMathematicalTransformation
    operation.formulaParam.value = formula
    operation.columnNameParam.value = columnName
    operation
  }
}
