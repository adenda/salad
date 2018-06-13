package com.adendamedia.salad

import com.adendamedia.salad.api._
import io.lettuce.core.api.async._
import io.lettuce.core.cluster.api.async.RedisClusterAsyncCommands

/**
  * Wrap the lettuce API to provide an idiomatic Scala API.
  * @see the composing traits for javadocs.
  * @param underlying The lettuce async API to be wrapped.
  * @tparam EK The key storage encoding.
  * @tparam EV The value storage encoding.
  * @tparam API The lettuce API to wrap.
  */
class SaladAPI[EK,EV,API]
(val underlying: API
  with RedisHashAsyncCommands[EK,EV]
  with RedisKeyAsyncCommands[EK,EV]
  with RedisStringAsyncCommands[EK,EV]
  with RedisServerAsyncCommands[EK,EV]
)
  extends SaladHashCommandsImpl[EK,EV,API]
    with SaladKeyCommandsImpl[EK,EV,API]
    with SaladStringCommandsImpl[EK,EV,API]
    with SaladServerCommandsImpl[EK,EV,API]

/**
  * Wrap the lettuce cluster-administration API to provide an idiomatic Scala API.
  *
  * @param underlying The lettuce async API to be wrapped.
  * @tparam EK The key storage encoding.
  * @tparam EV The value storage encoding.
  */
class SaladClusterAPI[EK,EV](val underlying: RedisClusterAsyncCommands[EK,EV])
  extends SaladClusterCommands[EK,EV,RedisClusterAsyncCommands[EK,EV]]
    with SaladKeyCommandsImpl[EK,EV,RedisKeyAsyncCommands[EK,EV]]
    with SaladServerCommandsImpl[EK,EV,RedisServerAsyncCommands[EK,EV]]
