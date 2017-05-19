package com.github.kliewkliew.salad

import java.util.concurrent.TimeUnit

import com.lambdaworks.redis.{ClientOptions, RedisClient}
import com.lambdaworks.redis.codec.ByteArrayCodec
import com.lambdaworks.redis.api.StatefulRedisConnection
import com.github.kliewkliew.salad.dressing.{SaladLoggingAPI, SaladServerCommandsAPI, SaladTimeoutAPI, SaladUIIDKeyAPI}

import scala.util.Try

object SaladConnection {

  private val codec = ByteArrayCodec.INSTANCE

  private object Standalone {
    type Connection = StatefulRedisConnection[Array[Byte],Array[Byte]]
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

  private val api = lettuceAPI.map { lettuce =>
    new SaladUIIDKeyAPI(
      new SaladLoggingAPI(
        new SaladTimeoutAPI(
          new SaladAPI(lettuce))))
  }

  private val serverCommandsApi = lettuceAPI.map { lettuce =>
      new SaladServerCommandsAPI(
        new SaladAPI(lettuce)
      )
  }

  lazy val saladApi = api.get
  lazy val saladServerCommandsApi = serverCommandsApi.get
}