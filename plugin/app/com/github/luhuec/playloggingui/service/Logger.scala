package com.github.luhuec.playloggingui.service

case class Logger(
    name: String,
    level: String,
    children: List[Logger],
    underlying: ch.qos.logback.classic.Logger
) {

  def withChild(logger: Logger): Logger = {
    val c = children.map(c => c.name -> c).toMap + (logger.name -> logger)
    this.copy(children = c.values.toList.sortBy(_.name))
  }
}
