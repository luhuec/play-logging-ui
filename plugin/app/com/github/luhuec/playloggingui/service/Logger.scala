package com.github.luhuec.playloggingui.service

import scala.collection.mutable.ListBuffer

case class Logger(
    name: String,
    level: String,
    children: ListBuffer[Logger],
    underlying: ch.qos.logback.classic.Logger
)
