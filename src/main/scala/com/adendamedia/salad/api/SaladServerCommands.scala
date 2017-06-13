package com.adendamedia.salad.api

import com.adendamedia.salad.serde.Serde

import scala.concurrent.{ExecutionContext, Future}

/**
  * Interface with executed commands for Redis Server Control
  * @tparam EK The key storage encoding.
  * @tparam EV The value storage encoding.
  * @tparam API The lettuce API to wrap.
  */
trait SaladServerCommands[EK, EV, API] {

  /**
    * Retrieve info about the Redis server node
    * @param section The section to retrieve info about the server. If left blank, returns info for all sections
    * @return A Future containing a string dump of the output of the command
    */
  def info(section: Option[String] = None)
                (implicit keySerde: Serde[String,EK], valSerde: Serde[String,String], executionContext: ExecutionContext)
  : Future[String]

}
