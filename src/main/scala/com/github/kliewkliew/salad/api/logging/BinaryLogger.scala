package com.github.kliewkliew.salad.api.logging

import com.typesafe.config.ConfigFactory
import org.slf4j.{Logger, LoggerFactory}

class BinaryLogger(namespace: String) {
  private val underlying = LoggerFactory.getLogger(namespace)
  protected val success = new SuccessLogger(underlying)
  protected val failure = new FailureLogger(underlying)
  protected val hardcoded: Logger = underlying
}

object LoggerConfig {
  private val config = ConfigFactory.load().getConfig("salad.logger")
  val failureLogLevel: String = Option.apply(config.getString("failure"))
    .getOrElse("WARN")
  val successLogLevel: String = Option.apply(config.getString("success"))
    .getOrElse("DEBUG")
}

class SuccessLogger(logger: Logger) {
  val log: String => Unit =
    LoggerConfig.successLogLevel.toUpperCase match {
      case "TRACE" => logger.trace
      case "DEBUG" => logger.debug
      case "INFO" => logger.info
      case "WARN" => logger.warn
      case "ERROR" => logger.error
    }
}

class FailureLogger(logger: Logger) {
  val log: (String, Throwable) => Unit =
    LoggerConfig.failureLogLevel.toUpperCase match {
      case "TRACE" => logger.trace
      case "DEBUG" => logger.debug
      case "INFO" => logger.info
      case "WARN" => logger.warn
      case "ERROR" => logger.error
    }
}