package com.github.luhuec.playloggingui.service

case class Tree(root: Logger) {

  def find(name: String): Option[Logger] = {
    if (name.toUpperCase == "ROOT") {
      Some(root)
    } else {
      name.split('.').toList.reverse match {
        case head :: Nil => root.children.find(l => l.name == head)
        case _ :: tail =>
          val parent = find(tail.reverse.mkString("."))
          parent.flatMap(p => p.children.find(l => l.name == name))
        case List("") => throw new Exception("logger name empty")
      }
    }
  }

  def +(logger: Logger): Tree = {
    val parentName = logger.name.split('.').dropRight(1).mkString(".")

    if (parentName.isEmpty) {
      this.copy(root = root.withChild(logger))
    } else {
      find(parentName) match {
        case Some(parent) => this + parent.withChild(logger)
        case None         => throw new Exception(s"logger '$parentName' not found")
      }
    }
  }

}

object Tree {
  def build(loggers: List[Logger]): Tree = {
    val root = loggers
      .find(l => l.name.toLowerCase == "root")
      .getOrElse(throw new Exception("no root logger found"))

    var tree = Tree(root)

    loggers
      .filter(_ != root)
      .sortWith(_.name.count(_ == '.') < _.name.count(_ == '.'))
      .foreach { logger =>
        tree = tree + logger
      }

    tree
  }
}
