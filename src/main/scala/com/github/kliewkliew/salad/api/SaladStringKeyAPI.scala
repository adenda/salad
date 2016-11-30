package com.github.kliewkliew.salad.api

import com.github.kliewkliew.salad.serde.Serde
import com.lambdaworks.redis.api.async._

import scala.concurrent.Future

/**
  * Wrap the lettuce API to provide an idiomatic Scala API.
  * The unencoded input key is always a String to be encoded to EK.
  * @see SaladAPI for javadocs per method.
  * @param commands The lettuce async API to be wrapped.
  * @tparam EK The key storage encoding.
  * @tparam EV The value storage encoding.
  * @tparam API The lettuce API to wrap.
  */
case class SaladStringKeyAPI[EK,EV,API]
(commands: API
  with RedisHashAsyncCommands[EK,EV]
  with RedisKeyAsyncCommands[EK,EV]
  with RedisStringAsyncCommands[EK,EV]
) {
  val api = SaladAPI(commands)

  def del(key: String)
         (implicit keySerde: Serde[String,EK])
  : Future[Boolean] =
    api.del(key)

  def expire(key: String, ex: Long)
            (implicit keySerde: Serde[String,EK])
  : Future[Boolean] =
    api.expire(key, ex)

  def pexpire(key: String, px: Long)
            (implicit keySerde: Serde[String,EK])
  : Future[Boolean] =
    api.pexpire(key, px)

  def persist(key: String)
                 (implicit keySerde: Serde[String,EK])
  : Future[Boolean] =
    api.persist(key)

  def get[DV](key: String)
             (implicit keySerde: Serde[String,EK], valSerde: Serde[DV,EV])
  : Future[Option[DV]] =
    api.get(key)

  def set[DV](key: String, value: DV,
              ex: Option[Long] = None, px: Option[Long] = None,
              nx: Boolean = false, xx: Boolean = false)
             (implicit keySerde: Serde[String,EK], valSerde: Serde[DV,EV])
  : Future[Boolean] =
    api.set(key, value, ex, px, nx, xx)

  def hdel(key: String, field: String)
              (implicit keySerde: Serde[String,EK])
  : Future[Boolean] =
    api.hdel(key, field)

  def hget[DV](key: String, field: String)
                 (implicit keySerde: Serde[String,EK], valSerde: Serde[DV,EV])
  : Future[Option[DV]] =
    api.hget(key, field)

  def hset[DV](key: String, field: String, value: DV,
                  nx: Boolean = false)
                 (implicit keySerde: Serde[String,EK], valSerde: Serde[DV,EV])
  : Future[Boolean] =
    api.hset(key, field, value, nx)

}