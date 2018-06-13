package com.adendamedia.salad

import java.util.concurrent.TimeUnit

import com.adendamedia.salad.dressing.{SaladLoggingAPI, SaladServerCommandsAPI, SaladTimeoutAPI, SaladUIIDKeyAPI}
import io.lettuce.core.{ClientOptions, RedisClient}
import io.lettuce.core.codec.ByteArrayCodec
import io.lettuce.core.api.StatefulRedisConnection

import scala.util.Try

object SaladConnection {

  private val codec = ByteArrayCodec.INSTANCE

  private object Standalone {
    type Connection = StatefulRedisConnection[Array[Byte],Array[Byte]]
    type ServerCommandsConnection = StatefulRedisConnection[String,String]
    val uri = "redis://127.0.0.1"
  }

  private val connection = Try {
    val client = RedisClient.create(Standalone.uri)
    RedisClient.create(Standalone.uri)
    client.setDefaultTimeout(1000, TimeUnit.MILLISECONDS)
    client.setOptions(ClientOptions.builder()
      .cancelCommandsOnReconnectFailure(true)
      .pingBeforeActivateConnection(true)
      .build())
    client.connect(codec)
  }

  private val lettuceAPI = connection map {
    case c => c.asInstanceOf[Standalone.Connection].async()
  }

  private val serverCommandslettuceAPI = connection map {
    case c => c.asInstanceOf[Standalone.ServerCommandsConnection].async()
  }

  private val api = lettuceAPI.map { lettuce =>
    new SaladUIIDKeyAPI(
      new SaladLoggingAPI(
        new SaladTimeoutAPI(
          new SaladAPI(lettuce))))
  }

  private val serverCommandsApi = serverCommandslettuceAPI.map { lettuce =>
      new SaladServerCommandsAPI(
        new SaladAPI(lettuce)
      )
  }

  lazy val saladApi = api.get
  lazy val saladServerCommandsApi = serverCommandsApi.get
}
