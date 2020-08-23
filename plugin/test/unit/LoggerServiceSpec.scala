package unit

import ch.qos.logback.classic
import ch.qos.logback.classic.Level
import org.mockito.IdiomaticMockito
import org.scalatest.EitherValues
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.slf4j.LoggerFactory
import service._

import scala.collection.mutable.ListBuffer

class LoggerServiceSpec extends AnyWordSpec with Matchers with IdiomaticMockito with EitherValues {

  "LoggerService" must {

    "update the log level of a logger" in new Fixture with SomeLoggers {
      classicLogger("play").getEffectiveLevel must not be Level.WARN
      service.updateLoglevel("play", "warn").right.value mustBe a[Unit]
      classicLogger("play").getEffectiveLevel mustBe Level.WARN
    }

    "return an error if logger not found" in new Fixture with SomeLoggers {
      service.updateLoglevel(logger = "foo", level = "info").left.value mustBe LoggerNotFound("foo")
    }

    "return an error if level not found" in new Fixture with SomeLoggers {
      service.updateLoglevel(logger = "play", level = "foo").left.value mustBe LevelNotFound("foo")
    }

    "return all loggers in a tree structure" in new Fixture with SomeLoggers {
      val loggers: List[Logger] = service.getLoggers

      loggers must have size 2

      val root: Logger = loggers.head
      root.name mustBe "ROOT"
      root.level mustBe "ROOT"
      root.children must have size 0

      val play: Logger = loggers(1)
      play.name mustBe "play"
      play.level mustBe "play"
      play.children must have size 1

      val play_core: Logger = play.children.head
      play_core.name mustBe "play.core"
      play_core.level mustBe "play.core"
      play_core.children must have size 2

      val play_core_cookie: Logger = play_core.children.head
      play_core_cookie.name mustBe "play.core.cookie"
      play_core_cookie.level mustBe "play.core.cookie"
      play_core_cookie.children must have size 0

      val play_core_http: Logger = play_core.children(1)
      play_core_http.name mustBe "play.core.http"
      play_core_http.level mustBe "play.core.http"
      play_core_http.children must have size 0
    }

    "not fail if loggers are not sorted" in new Fixture with ShuffledLoggers {
      val loggers: List[Logger] = service.getLoggers

      loggers must have size 1

      val play: Logger = loggers.head
      play.name mustBe "play"

      val play_core: Logger = play.children.head
      play_core.name mustBe "play.core"
      play_core.children must have size 2

      val play_core_http: Logger = play_core.children.head
      play_core_http.name mustBe "play.core.http"
      play_core_http.children must have size 0

      val play_core_cookie: Logger = play_core.children(1)
      play_core_cookie.name mustBe "play.core.cookie"
      play_core_cookie.children must have size 0
    }
  }

  private trait Fixture {

    val repo: LoggerRepo = mock[LoggerRepo]

    def classicLogger(name: String): classic.Logger = {
      LoggerFactory.getLogger(name).asInstanceOf[classic.Logger]
    }

    val service = new LoggerService(repo)
  }

  private trait SomeLoggers { this: Fixture =>
    repo.get returns List(
      Logger("ROOT", "ROOT", ListBuffer.empty, underlying = classicLogger("ROOT")),
      Logger("play", "play", ListBuffer.empty, underlying = classicLogger("play")),
      Logger("play.core", "play.core", ListBuffer.empty, underlying = classicLogger("play.core")),
      Logger(
        "play.core.cookie",
        "play.core.cookie",
        ListBuffer.empty,
        underlying = classicLogger("play.core.cookie")
      ),
      Logger(
        "play.core.http",
        "play.core.http",
        ListBuffer.empty,
        underlying = classicLogger("play.core.http")
      )
    )
  }

  private trait ShuffledLoggers { this: Fixture =>
    repo.get returns List(
      Logger(
        "play.core.http",
        "play.core.http",
        ListBuffer.empty,
        underlying = classicLogger("play.core.http")
      ),
      Logger("play.core", "play.core", ListBuffer.empty, underlying = classicLogger("play.core")),
      Logger(
        "play.core.cookie",
        "play.core.cookie",
        ListBuffer.empty,
        underlying = classicLogger("play.core.cookie")
      ),
      Logger("play", "play", ListBuffer.empty, underlying = classicLogger("play"))
    )
  }

}
