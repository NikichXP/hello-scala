package com.nikichxp.api

import java.util.concurrent.Executors

import akka.actor.typed.ActorSystem
import akka.util.Timeout

import scala.concurrent.ExecutionContext

abstract class RouterParent(implicit val system: ActorSystem[_]) {

  implicit val timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))
  implicit val ec = ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())

}
