package com.github.kliewkliew.salad.api

import ImplicitFutureConverters._

import com.github.kliewkliew.salad.serde.Serde
import com.lambdaworks.redis.api.async.RedisServerAsyncCommands

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

/**
  * Wrap the lettuce API to implement SaladServerCommands.
  * @see SaladServerCommands for javadocs.
  * @tparam EK The key storage encoding.
  * @tparam EV The value storage encoding.
  * @tparam API The lettuce API to wrap.
  */
trait SaladServerCommandsImpl[EK,EV,API] extends SaladServerCommands[EK,EK,API] {
  def underlying: API with RedisServerAsyncCommands[EK,EV]

  def info(section: Option[String] = None)
                (implicit keySerde: Serde[String,EK], valSerde: Serde[String,String], executionContext: ExecutionContext)
  : Future[String] = {

    def info1 = {
      Try(underlying.info()).toFuture
        .map(valSerde.deserialize)
    }

    def info2(section: String) = {
      Try(underlying.info(section)).toFuture
        .map(valSerde.deserialize)
    }

    section match {
      case Some(section: String) => info2(section)
      case None => info1
    }
  }

}
