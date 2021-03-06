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

package io.deepsense.deeplang

import io.deepsense.deeplang.doperables.Report
import io.deepsense.deeplang.doperables.dataframe.DataFrameMetadata
import org.json4s.JsonWriter
import spray.json._

/**
 * Objects of classes with this trait can be used in two ways.
 * 1. It can be object on which you can perform DOperations.
 * 2. It can be used to infer knowledge about objects that will be used later in workflow,
 * before it's execution.
 * In second case, only metadata field should be used.
 */
trait DOperable {

  /**
   * Type representing metadata knowledge for DOperable.
   */
  type M <: DOperable.AbstractMetadata

  /**
   * If this instance is used for execution, this field is set to None.
   * If it is used for inference, this contains inferred knowledge about this instance.
   */
  val inferredMetadata: Option[M] = None

  /** Exact metadata for this instance.
    * None means that we don't support metadata for this class in system.
    */
  def metadata: Option[M] = None

  def report(executionContext: ExecutionContext): Report

  /**
   * Saves DOperable on FS under specified path.
   * Sets url so that it informs where it has been saved.
   */
  def save(executionContext: ExecutionContext)(path: String): Unit

  /**
   * @return path where DOperable is stored, if None is returned then DOperable is not persisted.
   */
  def url: Option[String] = None

  /**
   * Called on executable version of object, produces an inferrable version of this object.
   * @return Inferrable version of this DOperable.
   */
  def toInferrable: DOperable
}

object DOperable {

  trait AbstractMetadata {
    /**
     * This method provides polymorphic behavior for JSON serialization.
     * Method toJson() generated by Spray does not support polymorphism.
     */
    final def serializeToJson: JsValue = wrap(getClass.getSimpleName, _serializeToJson)

    /**
     * This method should be overridden by this.toJson in subclasses.
     * The exact format of subclass' JSON should be defined in a separate JsonProtocol class.
     */
    protected def _serializeToJson: JsValue

    private def wrap(metadataType: String, content: JsValue): JsValue = {
      import AbstractMetadata._
      JsObject(TypeField -> JsString(metadataType), ContentField -> content)
    }
  }

  object AbstractMetadata {
    val TypeField = "type"
    val ContentField = "content"

    def unwrap(json: JsValue): JsValue = json.asJsObject.fields(ContentField)
  }
}
