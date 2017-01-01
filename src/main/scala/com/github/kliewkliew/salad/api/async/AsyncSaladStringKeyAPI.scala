package com.github.kliewkliew.salad.api.async

import com.github.kliewkliew.salad.serde.Serde
import com.lambdaworks.redis.api.async._
import com.lambdaworks.redis.cluster.api.async.RedisClusterAsyncCommands

import scala.concurrent.{ExecutionContext, Future}

/**
  * Wrap the lettuce API to provide an idiomatic Scala API.
  * The unencoded input key is always a String to be encoded to EK.
  * @see AsyncSaladAPI for javadocs per method.
  * @param underlying The lettuce async API to be wrapped.
  * @tparam EK The key storage encoding.
  * @tparam EV The value storage encoding.
  */
case class AsyncSaladStringKeyAPI[EK,EV,API]
(underlying: API
  with RedisClusterAsyncCommands[EK,EV]
  with RedisHashAsyncCommands[EK,EV]
  with RedisKeyAsyncCommands[EK,EV]
  with RedisStringAsyncCommands[EK,EV]
) {
  val api = AsyncSaladAPI(underlying)

  def del(key: String)
         (implicit keySerde: Serde[String,EK], executionContext: ExecutionContext)
  : Future[Boolean] =
    api.del(key)

  def expire(key: String, ex: Long)
            (implicit keySerde: Serde[String,EK], executionContext: ExecutionContext)
  : Future[Boolean] =
    api.expire(key, ex)

  def pexpire(key: String, px: Long)
            (implicit keySerde: Serde[String,EK], executionContext: ExecutionContext)
  : Future[Boolean] =
    api.pexpire(key, px)

  def persist(key: String)
                 (implicit keySerde: Serde[String,EK], executionContext: ExecutionContext)
  : Future[Boolean] =
    api.persist(key)

  def get[DV](key: String)
             (implicit keySerde: Serde[String,EK], valSerde: Serde[DV,EV], executionContext: ExecutionContext)
  : Future[Option[DV]] =
    api.get(key)

  def set[DV](key: String, value: DV,
              ex: Option[Long] = None, px: Option[Long] = None,
              nx: Boolean = false, xx: Boolean = false)
             (implicit keySerde: Serde[String,EK], valSerde: Serde[DV,EV], executionContext: ExecutionContext)
  : Future[Unit] =
    api.set(key, value, ex, px, nx, xx)

  def hdel(key: String, field: String)
              (implicit keySerde: Serde[String,EK], executionContext: ExecutionContext)
  : Future[Boolean] =
    api.hdel(key, field)

  def hget[DV](key: String, field: String)
                 (implicit keySerde: Serde[String,EK], valSerde: Serde[DV,EV], executionContext: ExecutionContext)
  : Future[Option[DV]] =
    api.hget(key, field)

  def hset[DV](key: String, field: String, value: DV,
                  nx: Boolean = false)
                 (implicit keySerde: Serde[String,EK], valSerde: Serde[DV,EV], executionContext: ExecutionContext)
  : Future[Boolean] =
    api.hset(key, field, value, nx)

}
