package service

import play.api.libs.json.{Json, Writes}

import scala.collection.mutable.ListBuffer

case class Logger(
    name: String,
    level: String,
    children: ListBuffer[Logger],
    underlying: ch.qos.logback.classic.Logger
)
