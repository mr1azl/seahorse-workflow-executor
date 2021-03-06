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

import org.mockito.Mockito.{verify, when}
import org.scalatest.mock.MockitoSugar
import org.scalatest.{Matchers, WordSpec}

import io.deepsense.deeplang.inference.{InferenceWarning, InferenceWarnings, InferContext}
import io.deepsense.deeplang.{DOperable, DKnowledge, DMethod1To1, ExecutionContext}
import io.deepsense.deeplang.doperables.Transformation
import io.deepsense.deeplang.doperables.dataframe.DataFrame

class ApplyTransformationSpec extends WordSpec with Matchers with MockitoSugar {

  "ApplyTransformation" should {
    "execute transform on transformation with dataframe as an argument" in {
      val applyTransformation = ApplyTransformation()
      val context = mock[ExecutionContext]
      val transformation = mock[Transformation]
      val inputDataFrame = mock[DataFrame]
      val resultDataFrame = mock[DataFrame]
      val transformMethodMock = mock[DMethod1To1[Unit, DataFrame, DataFrame]]

      when(transformation.transform) thenReturn transformMethodMock
      when(transformMethodMock.apply(context)(())(inputDataFrame)).thenReturn(resultDataFrame)

      val result = applyTransformation.execute(context)(Vector(transformation, inputDataFrame))
      result shouldBe Vector(resultDataFrame)
    }
  }

  it should {
    "infer result DataFrame correctly" in {
      val applyTransformation = ApplyTransformation()

      val dataFrameKnowledgeStub = mock[DKnowledge[DataFrame]]
      val inferContextStub = mock[InferContext]
      when(inferContextStub.fullInference).thenReturn(true)

      def transformationMock(): (Transformation, (DKnowledge[DOperable], InferenceWarnings)) = {
        val mocked = mock[Transformation]
        val transformMethodMock = mock[DMethod1To1[Unit, DataFrame, DataFrame]]
        val resultDataframeStub = mock[DataFrame]
        val resultDKnowledgeStub = DKnowledge(resultDataframeStub)
        val warningsStub = InferenceWarnings(mock[InferenceWarning])
        when(mocked.transform).thenReturn(transformMethodMock)
        when(transformMethodMock.infer(inferContextStub)(())(dataFrameKnowledgeStub))
          .thenReturn((resultDKnowledgeStub, warningsStub))
        (mocked, (resultDKnowledgeStub, warningsStub))
      }
      val (transformationMock1, (transformationResult1, warning1)) = transformationMock()
      val (transformationMock2, (transformationResult2, warning2)) = transformationMock()
      val inputKnowledge = Vector(
        DKnowledge[DOperable](transformationMock1, transformationMock2),
        dataFrameKnowledgeStub.asInstanceOf[DKnowledge[DOperable]])

      val (dKnowledge, warnings) = applyTransformation.inferKnowledge(
        inferContextStub)(inputKnowledge)

      dKnowledge shouldBe Vector(transformationResult1 ++ transformationResult2)
      warnings shouldBe (warning1 ++ warning2)
    }
  }
}
