/**
 * Copyright (c) 2015, CodiLime Inc.
 */

package io.deepsense.deeplang.doperables

import scala.concurrent.Future
import scala.util.Success

import org.scalatest.BeforeAndAfter

import io.deepsense.deeplang.doperables.factories.TrainedRidgeRegressionTestFactory
import io.deepsense.deeplang.{DeeplangIntegTestSupport, Model}
import io.deepsense.models.entities.{CreateEntityRequest, DataObjectReference, DataObjectReport, Entity}


class ModelDeploymentIntegSpec
  extends DeeplangIntegTestSupport
  with BeforeAndAfter
  with TrainedRidgeRegressionTestFactory {

  private val testFilePath: String = "/tests/modelDeploymentTest"

  before {
    rawHdfsClient.delete(testFilePath, true)
  }

  private def inputEntity = CreateEntityRequest(
    executionContext.tenantId,
    "name",
    "desc",
    "dclass",
    Some(DataObjectReference(testFilePath, "{}")),
    DataObjectReport("some report"),
    saved = true)

  "Model" should {
    "be deployable" in {
      val id: Entity.Id = DOperableSaver
        .saveDOperableWithEntityStorageRegistration(executionContext)(
          testTrainedRidgeRegression,
          inputEntity)

      val retrieved: Deployable = DOperableLoader.load(
        executionContext.entityStorageClient)(
        DeployableLoader.loadFromHdfs(executionContext.hdfsClient))(
        executionContext.tenantId, id)
      val response = "testId"
      val toService = (model: Model) => {
        import scala.concurrent.ExecutionContext.Implicits.global
        Future(response)
      }
      val deploymentResult = retrieved.deploy(toService)
      import scala.concurrent.ExecutionContext.Implicits.global
      deploymentResult.onComplete {
        case Success(value) => value shouldBe response
        case _ =>
      }
    }
  }


}