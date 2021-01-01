package com.github.luhuec.playloggingui.service

import cats.implicits._
import ch.qos.logback.classic.{Level, LoggerContext}
import javax.inject.{Inject, Singleton}
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

sealed trait Error
case class LoggerNotFound(logger: String) extends Error
case class LevelNotFound(level: String)   extends Error

@Singleton
class LoggerRepo {

  def get: List[Logger] = {
    val loggerContext =
      LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]

    loggerContext.getLoggerList.asScala.map { logger =>
      Logger(
        name = logger.getName,
        level = logger.getEffectiveLevel.toString,
        children = List.empty,
        underlying = logger
      )
    }.toList
  }

}

@Singleton
class LoggerService @Inject() (repo: LoggerRepo) {

  def getLoggers: List[Logger] = {
    val tree = Tree.build(repo.get)

    val root = tree.root.copy(children = List.empty)
    root :: tree.root.children
  }

  def updateLoglevel(loggerName: String, levelName: String): Either[Error, Unit] = {
    for {
      level  <- readLevel(levelName)
      logger <- Either.fromOption(Tree.build(repo.get).find(loggerName), LoggerNotFound(loggerName))
    } yield updateLogger(logger, level)
  }

  private def updateLogger(logger: Logger, level: Level): Unit = {
    logger.underlying.setLevel(level)
    logger.children.foreach(child => updateLogger(child, level))
  }

  private def readLevel(level: String): Either[Error, Level] = {
    level.toUpperCase match {
      case "OFF" | "DEBUG" | "INFO" | "WARN" | "ERROR" =>
        Right(Level.toLevel(level.toUpperCase, Level.INFO))
      case _ => Left(LevelNotFound(level))
    }
  }

}
