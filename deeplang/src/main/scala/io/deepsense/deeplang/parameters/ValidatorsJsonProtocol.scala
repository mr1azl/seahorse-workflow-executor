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

import scala.util.matching.Regex

import spray.json._

object ValidatorsJsonProtocol extends DefaultJsonProtocol {
  implicit object RegexJsonFormat extends JsonFormat[Regex] {
    override def write(regex: Regex): JsValue = regex.toString.toJson

    /**
     * This method is not implemented on purpose - RegexJsonFormat is only needed
     * for writing inside [[regexValidatorFormat]].
     */
    override def read(json: JsValue): Regex = ???

  }

  val rangeValidatorFormat = jsonFormat(
    RangeValidator, "begin", "end", "beginIncluded", "endIncluded", "step")
  val regexValidatorFormat = jsonFormat(RegexValidator, "regex")
}
