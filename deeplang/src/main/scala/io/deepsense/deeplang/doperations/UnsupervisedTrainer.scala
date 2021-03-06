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

import io.deepsense.commons.types.ColumnType
import io.deepsense.deeplang.doperables.dataframe._
import io.deepsense.deeplang.doperables.{Scorable, UnsupervisedTrainable, UnsupervisedTrainableParameters}
import io.deepsense.deeplang.inference.{InferContext, InferenceWarnings}
import io.deepsense.deeplang.{DOperation2To2, _}

trait UnsupervisedTrainer[T1 <: UnsupervisedTrainable, T2 <: Scorable]
  extends DOperation2To2[T1, DataFrame, T2, DataFrame]
  with UnsupervisedTrainableParameters {

  override val parameters = trainParameters

  override val dataFramePortIndex = 1

  override protected def _execute(context: ExecutionContext)
                                 (trainable: T1, dataframe: DataFrame): (T2, DataFrame) = {
    val trainedModel = trainable.train(context)(this)(dataframe).asInstanceOf[T2]
    (trainedModel, trainedModel.score.apply(context)(predictionColumnName(dataframe))(dataframe))
  }

  override protected def _inferKnowledge(context: InferContext)(
    trainableKnowledge: DKnowledge[T1],
    dataframeKnowledge: DKnowledge[DataFrame])
    : ((DKnowledge[T2], DKnowledge[DataFrame]), InferenceWarnings) = {

    val modelKnowledge = for {
      trainable <- trainableKnowledge.types
      (result, _) = trainable.train.infer(context)(this)(dataframeKnowledge)
    } yield result.asInstanceOf[DKnowledge[T2]]

    val resultDataFrameKnowledge = inferOutputDataFrameKnowledge(
      dataframeKnowledge, predictionColumnParameter.value)

    ((DKnowledge(modelKnowledge), resultDataFrameKnowledge), InferenceWarnings.empty)
  }

  private def inferOutputDataFrameKnowledge(
      inputKnowledge: DKnowledge[DataFrame],
      predictionColumnName: String): DKnowledge[DataFrame] = {

    val newColumn = CommonColumnMetadata(
      predictionColumnName, index = None, columnType = Some(ColumnType.numeric))
    val outputMetadata = inputKnowledge.types.head.inferredMetadata.get.appendColumn(newColumn)
    DKnowledge(DataFrameBuilder.buildDataFrameForInference(outputMetadata))
  }
}
