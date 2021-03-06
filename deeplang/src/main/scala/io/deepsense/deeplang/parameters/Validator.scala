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

package io.deepsense.deeplang.parameters

import spray.json._

import io.deepsense.deeplang.exceptions.DeepLangException
import io.deepsense.deeplang.parameters.ValidatorType.ValidatorType

/** Represents anything that validates parameter. */
@SerialVersionUID(1)
trait Validator[ParameterType] extends Serializable {
  val validatorType: ValidatorType

  def validate(parameter: ParameterType): Vector[DeepLangException]

  final def toJson: JsObject = {
    import DefaultJsonProtocol._
    JsObject(
      "type" -> validatorType.toString.toJson,
      "configuration" -> configurationToJson)
  }

  protected def configurationToJson: JsObject
}
