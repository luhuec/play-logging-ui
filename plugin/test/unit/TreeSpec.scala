package unit

import ch.qos.logback.classic
import com.github.luhuec.playloggingui.service.{Logger, Tree}
import org.scalatest.{MustMatchers, OptionValues, WordSpec}
import org.slf4j.LoggerFactory

class TreeSpec extends WordSpec with MustMatchers with OptionValues {

  "Tree" must {
    "find loggers" in new SomeLoggers {
      val root: Logger = Logger(
        name = "ROOT",
        level = "INFO",
        children = List(play, akka),
        underlying = classicLogger("ROOT")
      )
      val tree: Tree = Tree(root = root)

      tree.find("root").value mustBe root
      tree.find("akka").value mustBe akka
      tree.find("akka.http").value mustBe `akka.http`
      tree.find("play").value mustBe play
      tree.find("play.core").value mustBe `play.core`
      tree.find("play.core.cookie").value mustBe `play.core.cookie`
      tree.find("play.core.http").value mustBe `play.core.http`

      tree.find("foo") mustBe None
      tree.find("foo.baz.bar") mustBe None
    }

    "add a logger" in new SomeLoggers {
      val root: Logger = Logger(
        name = "ROOT",
        level = "INFO",
        children = List(play),
        underlying = classicLogger("ROOT")
      )
      val tree: Tree = Tree(root = root) + akka
      tree.root.children mustBe List(akka, play)
    }

    "replace an existing logger" in new SomeLoggers {
      val root: Logger = Logger(
        name = "ROOT",
        level = "INFO",
        children = List(akka, play),
        underlying = classicLogger("ROOT")
      )
      val tree: Tree = Tree(root = root) + akka
      tree.root.children mustBe List(akka, play)
    }

    "replace another existing logger" in new SomeLoggers {
      val root: Logger = Logger(
        name = "ROOT",
        level = "INFO",
        children = List(play),
        underlying = classicLogger("ROOT")
      )
      val tree: Tree = Tree(root = root) + `play.core.cookie`
      tree.find("play.core").value.children mustBe List(`play.core.cookie`, `play.core.http`)
    }

    "add another logger" in new SomeLoggers {
      val `play.core.filter`: Logger =
        Logger(
          "play.core.filter",
          level = "INFO",
          List.empty,
          classicLogger("play.core.filter")
        )

      val root: Logger = Logger(
        name = "ROOT",
        level = "INFO",
        children = List(play, akka),
        underlying = classicLogger("ROOT")
      )
      val tree: Tree = Tree(root = root) + `play.core.filter`

      tree.find("play.core").value.children mustBe List(
        `play.core.cookie`,
        `play.core.filter`,
        `play.core.http`
      )
    }

    "build a tree from a list of loggers" in new Fixture {
      val loggers: List[Logger] = List(
        "akka.http",
        "akka.core",
        "akka.http.tracing",
        "ROOT",
        "play.core.cookie",
        "play.core",
        "play.core.http",
        "play",
        "akka"
      ) map { name =>
        Logger(name, level = "INFO", List.empty, classicLogger(name))
      }

      val tree: Tree = Tree.build(loggers)

      tree.find("root").value.children.map(_.name) mustBe List("akka", "play")

      tree.find("play").value.children.map(_.name) mustBe List("play.core")
      tree.find("play.core").value.children.map(_.name) mustBe List(
        "play.core.cookie",
        "play.core.http"
      )
      tree.find("play.core.cookie").value.children mustBe empty
      tree.find("play.core.http").value.children mustBe empty

      tree.find("akka").value.children.map(_.name) mustBe List("akka.core", "akka.http")
      tree.find("akka.core").value.children mustBe empty
      tree.find("akka.http").value.children.map(_.name) mustBe List("akka.http.tracing")
      tree.find("akka.http.tracing").value.children mustBe empty
    }
  }

  private trait Fixture {
    def classicLogger(name: String): classic.Logger = {
      LoggerFactory.getLogger(name).asInstanceOf[classic.Logger]
    }
  }

  private trait SomeLoggers extends Fixture {

    val `akka.http`: Logger =
      Logger("akka.http", level = "INFO", List.empty, classicLogger("akka.http"))

    val akka: Logger =
      Logger("akka", level = "INFO", List(`akka.http`), classicLogger("akka"))

    val `play.core.http`: Logger =
      Logger("play.core.http", level = "INFO", List.empty, classicLogger("play.core.http"))
    val `play.core.cookie`: Logger =
      Logger(
        "play.core.cookie",
        level = "INFO",
        List.empty,
        classicLogger("play.core.cookie")
      )

    val `play.core`: Logger =
      Logger(
        "play.core",
        level = "INFO",
        List(`play.core.cookie`, `play.core.http`),
        classicLogger("play.core")
      )

    val `play`: Logger =
      Logger(
        "play",
        level = "INFO",
        List(`play.core`),
        classicLogger("play")
      )
  }

}
