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

package io.deepsense.deeplang.inference

import io.deepsense.deeplang.doperables.dataframe.DataFrameMetadata
import io.deepsense.deeplang.parameters.{SingleColumnSelection, ColumnSelection}

/**
 * Container for inference warnings.
 */
case class InferenceWarnings(warnings: Vector[InferenceWarning]) {
  def :+(warning: InferenceWarning): InferenceWarnings =
    InferenceWarnings(warnings :+ warning)

  def ++(other: InferenceWarnings): InferenceWarnings =
    InferenceWarnings(warnings ++ other.warnings)

  def isEmpty(): Boolean = warnings.isEmpty
}

object InferenceWarnings {
  def empty: InferenceWarnings = InferenceWarnings(Vector.empty[InferenceWarning])

  def apply(warnings: InferenceWarning*): InferenceWarnings = InferenceWarnings(warnings.toVector)

  def flatten(inferenceWarnings: Vector[InferenceWarnings]): InferenceWarnings =
    InferenceWarnings(inferenceWarnings.map(_.warnings).flatten)
}
