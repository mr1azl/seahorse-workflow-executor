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

package io.deepsense.models.workflows

import io.deepsense.graph.StatefulGraph

case class WorkflowWithSavedResults(
  id: Workflow.Id,
  metadata: WorkflowMetadata,
  graph: StatefulGraph,
  thirdPartyData: ThirdPartyData,
  executionReport: ExecutionReportWithId)

object WorkflowWithSavedResults {
  def apply(
      resultsId: ExecutionReportWithId.Id,
      workflowWithResults: WorkflowWithResults): WorkflowWithSavedResults = {
    new WorkflowWithSavedResults(
      workflowWithResults.id,
      workflowWithResults.metadata,
      workflowWithResults.graph,
      workflowWithResults.thirdPartyData,
      ExecutionReportWithId(resultsId, workflowWithResults.executionReport))
  }
}
