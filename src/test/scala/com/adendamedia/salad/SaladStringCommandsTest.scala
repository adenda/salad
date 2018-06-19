package com.adendamedia.salad

import org.scalatest.AsyncFlatSpec
import com.adendamedia.salad.serde.ByteArraySerdes._

import scala.concurrent.Future
import scala.util.Random
import SaladConnection._

class SaladStringCommandsTest extends AsyncFlatSpec {

  private val api = saladApi

  behavior of "Salad string commands"

  private val generator = new Random()
  private val num = generator.nextInt().toString

  it should "set key" in {
    val setFoo: Future[Unit] = api.set[String]("foobar", num)
    setFoo map(_ => assert(true))
  }

  it should "get key" in {
    val future = api.get[String]("foobar")
    future map { v => assert(v.contains(num)) }
  }

}
