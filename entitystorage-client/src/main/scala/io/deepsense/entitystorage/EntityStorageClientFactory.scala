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

package io.deepsense.entitystorage

import scala.concurrent.Await

import akka.actor.{ActorSystem, ActorRef}
import akka.util.Timeout
import com.typesafe.config.ConfigFactory

trait ActorBasedEntityStorageClientFactory {
  def create(actorRef: ActorRef): EntityStorageClient
}

trait EntityStorageClientFactory extends AutoCloseable {

  def create(
    actorSystemName: String,
    hostname: String,
    port: Int,
    actorName: String,
    timeoutSeconds: Int): EntityStorageClient
}

case class EntityStorageClientFactoryImpl(
    host: String = "localhost",
    port: Int = 0)
  extends EntityStorageClientFactory {

  import scala.collection.JavaConverters._
  val actorSystem = ActorSystem(
    "EntityStorageClient",
    ConfigFactory.parseMap(
      Map(
        "akka.actor.provider" -> "akka.remote.RemoteActorRefProvider",
        "akka.remote.netty.tcp.port" -> port.toString,
        "akka.remote.hostname" -> host
      ).asJava
    )
  )

  override def create(actorSystemName: String, hostname: String, port: Int, actorName: String,
    timeoutSeconds: Int): EntityStorageClient = {
    val path = s"akka.tcp://$actorSystemName@$hostname:$port/user/$actorName"

    import scala.concurrent.duration._
    implicit val timeout = Timeout(timeoutSeconds.seconds)

    val actorRef =
      Await.result(actorSystem.actorSelection(path).resolveOne(), timeoutSeconds.seconds)
    new ActorBasedEntityStorageClient(actorRef)
  }

  override def close(): Unit = actorSystem.shutdown()
}
