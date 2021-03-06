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

package io.deepsense.deeplang.doperables.dataframe

import org.apache.spark.sql.types.{StructField, StructType}

object SchemaPrintingUtils {

  def structTypeToString(structType: StructType): String = structType.zipWithIndex.map {
    case (field, index) => structFieldToString(field, index)
  }.mkString("[", ",", "]")

  def columnsMetadataToSchemaString(columns: Seq[ColumnMetadata]): String =
    columns.map(_.prettyPrint).mkString("[", ",", "]")

  def structFieldToString(structField: StructField, index: Int): String =
    structFieldToString(structField, index.toString)

  def columnMetadataToString(columnMetadata: ColumnMetadata): String = structFieldToString(
    columnMetadata.toStructField,
    columnMetadata.index.map(_.toString).getOrElse("_"))

  private def structFieldToString(structField: StructField, index: String): String =
    s"($index -> ${structField.name}: ${structField.dataType})"
}
