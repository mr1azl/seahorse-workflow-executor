/**
 * Copyright (c) 2015, CodiLime Inc.
 */

package io.deepsense.deeplang.doperations

import io.deepsense.deeplang.doperables.Scorable
import io.deepsense.deeplang.doperables.dataframe.DataFrame
import io.deepsense.deeplang.inference.{InferenceWarnings, InferContext}
import io.deepsense.deeplang.parameters.{SingleColumnCreatorParameter, ParametersSchema}
import io.deepsense.deeplang.{DKnowledge, DOperation2To1, ExecutionContext}

trait Scorer[T <: Scorable] extends DOperation2To1[T, DataFrame, DataFrame] {

  val predictionColumnParam = SingleColumnCreatorParameter(
    "Column name for predictions",
    Some("prediction"),
    required = true)

  override val parameters = ParametersSchema(
    "prediction column" -> predictionColumnParam
  )

  override protected def _execute(
      context: ExecutionContext)(
      scorable: T, dataframe: DataFrame): DataFrame = {
    scorable.score(context)(predictionColumnParam.value.get)(dataframe)
  }

  override protected def _inferFullKnowledge(
      context: InferContext)(
      scorableKnowledge: DKnowledge[T], dataFrameKnowledge: DKnowledge[DataFrame])
      : (DKnowledge[DataFrame], InferenceWarnings) = {
    val inferenceResults = for (scorable <- scorableKnowledge.types)
      yield scorable.score.infer(context)(predictionColumnParam.value.get)(dataFrameKnowledge)
    val (inferredDataFrameKnowledge, inferenceWarnings) = inferenceResults.unzip
    (DKnowledge(inferredDataFrameKnowledge), InferenceWarnings.flatten(inferenceWarnings.toVector))
  }
}