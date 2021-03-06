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

package io.deepsense.graph

case class Endpoint(nodeId: Node.Id, portIndex: Int)

case class Edge(from: Endpoint, to: Endpoint)

object Edge {
  def apply(node1: Node, portIndex1: Int, node2: Node, portIndex2: Int): Edge =
    Edge(Endpoint(node1.id, portIndex1), Endpoint(node2.id, portIndex2))
}
