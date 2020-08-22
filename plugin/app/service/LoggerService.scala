package service

import ch.qos.logback.classic.{Level, LoggerContext}
import controllers.MyLogger
import javax.inject.{Inject, Singleton}
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer

class LoggerRepo {

  def get: List[MyLogger] = {
    val loggerContext =
      LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]

    loggerContext.getLoggerList.asScala.map { logger =>
      MyLogger(
        name = logger.getName,
        level = logger.getEffectiveLevel.toString,
        children = ListBuffer.empty
      )
    }.toList
  }

}

@Singleton
class LoggerService @Inject() (repo: LoggerRepo) {

  def getLoggers: List[MyLogger] = {

    var loggers = ListBuffer.empty[MyLogger]

    repo.get
      .sortWith {
        _.name.count(_ == '.') < _.name.count(_ == '.')
      }
      .foreach { logger =>
        if (logger.name.count(_ == '.') == 0) {
          loggers = loggers += logger
        } else {

          var parent: MyLogger =
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

  def updateLoglevel(logger: String, level: String): Unit = {
    val loggerContext =
      LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
    loggerContext.getLoggerList.asScala
      .find(_.getName == logger)
      .map(l => {
        l.setLevel(Level.toLevel(level.toUpperCase, Level.INFO))
        l
      })
  }

}
