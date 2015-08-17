/**
 * Copyright (c) 2015, CodiLime Inc.
 */

package io.deepsense.deeplang.doperables

import io.deepsense.deeplang.parameters.{ColumnSelectorParameter, ParametersSchema, SingleColumnSelectorParameter}

trait WithTrainParameters {
  private val featureColumnsField = "feature columns"

  private val targetColumnField = "target column"

  protected val dataFramePortIndex: Int

  protected val trainParameters = ParametersSchema(
    featureColumnsField -> ColumnSelectorParameter(
      "Columns which are to be used as features in regression",
      required = true,
      portIndex = dataFramePortIndex),
    targetColumnField -> SingleColumnSelectorParameter(
      "Column against which the regression will be performed",
      required = true,
      portIndex = dataFramePortIndex))

  protected def parametersForTrainable: Trainable.Parameters = Trainable.Parameters(
    trainParameters.getColumnSelection(featureColumnsField),
    trainParameters.getSingleColumnSelection(targetColumnField))
}