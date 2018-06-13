package com.adendamedia.salad

import org.scalatest.AsyncFlatSpec
import com.adendamedia.salad.serde.StringSerdes._
import com.adendamedia.salad.dressing.SaladServerCommandsAPI._

import scala.concurrent.Future
import SaladConnection._

class SaladServerCommandsTest extends AsyncFlatSpec {

  private val api = saladServerCommandsApi

  behavior of "server commands"

  it should "retrieve 'memory' server info" in {
    val info: Future[ServerInfo] = api.info(Some("memory"))
    info map { map =>
      assert(map("memory")("maxmemory").nonEmpty)
    }
  }

  it should "retrieve all server info" in {
    val info: Future[ServerInfo] = api.info()
    info map { map =>
      assert(map("memory")("maxmemory").nonEmpty)
      assert(map("server")("redis_mode").nonEmpty)
      assert(map("cpu")("used_cpu_sys").nonEmpty)
    }
  }

}
