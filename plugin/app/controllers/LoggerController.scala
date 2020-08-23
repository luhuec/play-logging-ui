package controllers

import javax.inject.{Inject, Singleton}
import play.api.libs.json.{Format, Json}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import service.{LevelNotFound, LoggerNotFound, LoggerService}

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class MyLogger(name: String, level: String, children: ListBuffer[MyLogger])

@Singleton
class LoggerController @Inject() (cc: ControllerComponents, loggerService: LoggerService)
    extends AbstractController(cc) {

  def index: Action[AnyContent] =
    Action.async { request =>
      Future {
        val basepath = request.path.split("/playloggingui").headOption.getOrElse("")
        Ok(views.html.index(basepath))
      }

    }

  def updateLogger(logger: String, level: String): Action[AnyContent] =
    Action.async {
      Future {
        loggerService.updateLoglevel(logger = logger, level = level) match {
          case Right(_)                => NoContent
          case Left(LoggerNotFound(l)) => BadRequest(s"Logger '$l' not found")
          case Left(LevelNotFound(l))  => BadRequest(s"Level '$l' not found")
        }
      }
    }

  def loggers: Action[AnyContent] =
    Action.async {
      Future {
        implicit val format: Format[MyLogger] = Json.format[MyLogger]
        val loggers                           = loggerService.getLoggers
        Ok(Json.toJson(loggers))
      }
    }
}
