package service

import ch.qos.logback.classic.{Level, LoggerContext}
import javax.inject.{Inject, Singleton}
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer

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
        children = ListBuffer.empty,
        underlying = logger
      )
    }.toList
  }

}

@Singleton
class LoggerService @Inject() (repo: LoggerRepo) {

  def getLoggers: List[Logger] = {

    var loggers = ListBuffer.empty[Logger]

    repo.get
      .sortWith {
        _.name.count(_ == '.') < _.name.count(_ == '.')
      }
      .foreach { logger =>
        if (logger.name.count(_ == '.') == 0) {
          loggers = loggers += logger
        } else {

          var parent: Logger =
            loggers.find(_.name == logger.name.split('.').head).get

          for (i <- 2 to logger.name.count(_ == '.')) {
            val parentName =
              logger.name.split('.').toList.slice(0, i).mkString(".")
            parent = parent.children.find(_.name == parentName).get
          }

          parent.children += logger

        }

      }

    loggers.toList
  }

  def updateLoglevel(logger: String, level: String): Either[Error, Unit] = {
    level.toUpperCase match {
      case "OFF" | "DEBUG" | "INFO" | "WARN" | "ERROR" =>
        val result = repo.get
          .find(_.name == logger)
          .map(l => {
            l.underlying.setLevel(Level.toLevel(level.toUpperCase, Level.INFO))
            l
          })

        result match {
          case Some(_) => Right(())
          case None    => Left(LoggerNotFound(logger))
        }
      case _ => Left(LevelNotFound(level))
    }

  }

}
