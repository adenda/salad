package com.github.kliewkliew.salad.dressing

import com.github.kliewkliew.salad.api.SaladServerCommands
import com.github.kliewkliew.salad.serde.Serde

import scala.concurrent.{ExecutionContext, Future}

object SaladServerCommandsAPI {
  type ServerInfo = Map[String, Map[String, String]]
}

/**
  * API for Salad server commands
  * @see the composing traits for javadocs.
  * @param underlying The lettuce async API to be wrapped.
  * @tparam EK The key storage encoding.
  * @tparam EV The value storage encoding.
  */
class SaladServerCommandsAPI[EK,EV,SALAD,LETTUCE](val underlying: SALAD with SaladServerCommands[EK,EV,LETTUCE]) {
  import SaladServerCommandsAPI._

  /**
    * Retrieve info about the server
    * @param section The section to retrieve info about. If left blank retrieves server info for all sections
    * @return A Future containing a Map of the retrieved server info. Keys of this map are server info sections. Values
    *         of this map are Maps, with key-values corresponding to info section fields.
    */
  def info(section: Option[String] = None)
              (implicit keySerde: Serde[String,EK], valSerde: Serde[String,String], executionContext: ExecutionContext)
  : Future[ServerInfo] = {

    def getServerInfoForSection(result: Future[String], s: String): Future[ServerInfo] = {
      result.map { lines =>
        val sectionMap = parseSection(lines)
        Map(s -> sectionMap)
      }
    }

    def parseSection(lines: String): Map[String,String] = {
      val parts = lines.split(Array('\n')).tail.map(_.stripSuffix("\r"))
      val emptySection = Map.empty[String, String]
      parts.foldLeft(emptySection) { case (map, part) =>
        if (part.nonEmpty) {
          val parts = part.split(':')
          val key = parts(0)
          val value = parts(1)
          map + (key -> value)
        }
        else map
      }
    }

    def getServerInfoForAllSections(result: Future[String]): Future[ServerInfo] = {
      result.map { i =>
        val regex = "# \\w+( \\w+)*"
        val r = "# \\w+( \\w+)*".r
        val headings = r.findAllIn(i).map(_.stripPrefix("# ")).toList
        val sectionRows = i.split(regex).toList.tail
        val sectionsZip = headings zip sectionRows
        val emptySections = Map.empty[String, Map[String,String]]
        sectionsZip.foldLeft(emptySections) { case (map, (sectionHeading, sectionLines)) =>
          val section = parseSection(sectionLines)
          map + (sectionHeading.toLowerCase -> section)
        }
      }
    }

    val result = underlying.info(section)

    section match {
      case Some(s) => getServerInfoForSection(result, s)
      case None => getServerInfoForAllSections(result)
    }
  }

}
