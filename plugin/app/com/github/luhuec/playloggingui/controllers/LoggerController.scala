package com.github.luhuec.playloggingui.controllers

import com.github.luhuec.playloggingui.service.{LevelNotFound, LoggerNotFound, LoggerService}
import javax.inject.{Inject, Singleton}
import play.api.Environment
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import play.twirl.api.Html

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.Source

@Singleton
class LoggerController @Inject() (
    cc: ControllerComponents,
    loggerService: LoggerService,
    environment: Environment
) extends AbstractController(cc) {

  def index: Action[AnyContent] =
    Action.async { request =>
      Future {
        val basepath = request.path.split("/playloggingui").headOption.getOrElse("")
        val js = environment.resourceAsStream("playloggingui/assets/main.js").map { is =>
          Html(Source.fromInputStream(is).mkString)
        } getOrElse (throw new Exception("Could not load js"))
        Ok(com.github.luhuec.playloggingui.views.html.index(basepath, js))
      }
    }

  def updateLogger(logger: String, level: String): Action[AnyContent] =
    Action.async {
      Future {
        loggerService.updateLoglevel(loggerName = logger, levelName = level) match {
          case Right(_)                => NoContent
          case Left(LoggerNotFound(l)) => BadRequest(s"Logger '$l' not found")
          case Left(LevelNotFound(l))  => BadRequest(s"Level '$l' not found")
        }
      }
    }

  def loggers: Action[AnyContent] =
    Action.async {
      Future {
        val loggers: List[LoggerResponse] = loggerService.getLoggers.map(LoggerResponse.create)
        Ok(Json.toJson(loggers))
      }
    }
}
