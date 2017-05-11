package com.github.kliewkliew.salad.dressing.logging

import scala.util.{Failure, Success, Try}

object SaladStringCommandLogger extends BinaryLogger("SaladStringCommands") {

  def get[DK,DV](key: DK)
                (result: Try[Option[DV]]) =
    result match {
      case Success(Some(value)) =>
        success.log(s"Got key, value: $key, $value")
      case Success(None) =>
        success.log(s"No value for key: $key")
      case Failure(t) =>
        failure.log(
          s"Failed to get value for key: $key", t)
    }

  def set[DK,DV](key: DK, value: DV,
                 ex: Option[Long], px: Option[Long],
                 nx: Boolean, xx: Boolean)
                (result: Try[Unit]) =
    result match {
      case Success(_) =>
        success.log(s"Set key, value: $key, $value")
      case Failure(t) =>
        failure.log(
          s"Failed to set key, value: $key, $value", t)
    }

  def incr[DK](key: DK) (result: Try[Option[Long]]) =
    result match {
      case Success(Some(value)) =>
        success.log(s"Incremented key: $key")
      case Success(None) =>
        success.log(s"No value for key: $key")
      case Failure(t) =>
        failure.log(
          s"Failed to increment key: $key", t)
    }

  def decr[DK](key: DK) (result: Try[Option[Long]]) =
    result match {
      case Success(Some(value)) =>
        success.log(s"Decremented key: $key")
      case Success(None) =>
        success.log(s"No value for key: $key")
      case Failure(t) =>
        failure.log(
          s"Failed to decrement key: $key", t)
    }
}
