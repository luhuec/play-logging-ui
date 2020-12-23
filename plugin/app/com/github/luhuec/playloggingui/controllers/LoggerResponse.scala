package com.github.luhuec.playloggingui.controllers

import com.github.luhuec.playloggingui.service.Logger
import play.api.libs.json.{Json, Writes}

case class LoggerResponse(name: String, level: String, children: List[LoggerResponse])

object LoggerResponse {
  implicit val writes: Writes[LoggerResponse] = Json.writes[LoggerResponse]

  def create(logger: Logger): LoggerResponse = {
    LoggerResponse(
      name = logger.name,
      level = logger.level,
      children = logger.children.map(LoggerResponse.create).toList
    )
  }

}
