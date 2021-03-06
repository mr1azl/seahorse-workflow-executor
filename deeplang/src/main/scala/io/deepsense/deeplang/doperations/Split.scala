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

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Row

import io.deepsense.deeplang._
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.inference.{InferContext, InferenceWarnings}
import io.deepsense.deeplang.parameters.{NumericParameter, ParametersSchema, RangeValidator}

case class Split() extends DOperation1To2[DataFrame, DataFrame, DataFrame] {
  override val name: String = "Split"
  override val id: DOperation.Id = "d273c42f-b840-4402-ba6b-18282cc68de3"

  val splitRatioParam = NumericParameter(
    "Percentage of rows that should end up in the first output DataFrame",
    default = Some(0.5),
    RangeValidator(0.0, 1.0, true, true),
    _value = None
  )

  val seedParam = NumericParameter(
    "Seed for random generator used during splitting",
    default = Some(1.0),
    // TODO Fix RangeValidator, because now it can't handle Int.MinValue and Int.MaxValue
    RangeValidator(Int.MinValue / 2, Int.MaxValue / 2, true, true, Some(1.0)),
    _value = None
  )

  override protected def _execute(context: ExecutionContext)
                                 (df: DataFrame): (DataFrame, DataFrame) = {
    val range: Double = splitRatioParam.value
    val seed: Long = seedParam.value.toLong
    val Array(f1: RDD[Row], f2: RDD[Row]) = split(df, range, seed)
    val schema = df.sparkDataFrame.schema
    val dataFrame1 = context.dataFrameBuilder.buildDataFrame(schema, f1)
    val dataFrame2 = context.dataFrameBuilder.buildDataFrame(schema, f2)
    (dataFrame1, dataFrame2)
  }

  def split(df: DataFrame, range: Double, seed: Long): Array[RDD[Row]] = {
    df.sparkDataFrame.rdd.randomSplit(Array(range, 1.0 - range), seed)
  }

  override val parameters: ParametersSchema = ParametersSchema(
    "split ratio" -> splitRatioParam,
    "seed" -> seedParam
  )

  override protected def _inferFullKnowledge(context: InferContext)
      (knowledge: DKnowledge[DataFrame]):
      ((DKnowledge[DataFrame], DKnowledge[DataFrame]), InferenceWarnings) = {
    ((knowledge, knowledge), InferenceWarnings.empty)
  }

  @transient
  override lazy val tTagTI_0: ru.TypeTag[DataFrame] = ru.typeTag[DataFrame]
  @transient
  override lazy val tTagTO_0: ru.TypeTag[DataFrame] = ru.typeTag[DataFrame]
  @transient
  override lazy val tTagTO_1: ru.TypeTag[DataFrame] = ru.typeTag[DataFrame]
}

object Split {
  def apply(splitRatio: Double, seed: Long): Split = {
    val splitter = new Split
    splitter.splitRatioParam.value = splitRatio
    splitter.seedParam.value = seed
    splitter
  }
}
