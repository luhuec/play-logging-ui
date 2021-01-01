package unit

import ch.qos.logback.classic
import ch.qos.logback.classic.Level
import com.github.luhuec.playloggingui.service._
import org.mockito.IdiomaticMockito
import org.scalatest.{EitherValues, MustMatchers, WordSpec}
import org.slf4j.LoggerFactory

class LoggerServiceSpec extends WordSpec with IdiomaticMockito with EitherValues with MustMatchers {

  "LoggerService" must {

    "update the log level of a logger" in new Fixture with SomeLoggers {
      classicLogger("play").getEffectiveLevel must not be Level.WARN
      service.updateLoglevel("play", "warn").right.value mustBe a[Unit]
      classicLogger("play").getEffectiveLevel mustBe Level.WARN
    }

    "update the log level of a logger and it's children" in new Fixture with SomeLoggers {
      classicLogger("play").getEffectiveLevel must not be Level.DEBUG
      classicLogger("play.core").getEffectiveLevel must not be Level.DEBUG
      classicLogger("play.core.cookie").getEffectiveLevel must not be Level.DEBUG
      classicLogger("play.core.http").getEffectiveLevel must not be Level.DEBUG

      service.updateLoglevel("play.core", "debug").right.value mustBe a[Unit]

      classicLogger("play").getEffectiveLevel must not be Level.DEBUG
      classicLogger("play.core").getEffectiveLevel mustBe Level.DEBUG
      classicLogger("play.core.cookie").getEffectiveLevel mustBe Level.DEBUG
      classicLogger("play.core.http").getEffectiveLevel mustBe Level.DEBUG
    }

    "return an error if logger not found" in new Fixture with SomeLoggers {
      service
        .updateLoglevel(loggerName = "foo", levelName = "info")
        .left
        .value mustBe LoggerNotFound("foo")
    }

    "return an error if level not found" in new Fixture with SomeLoggers {
      service
        .updateLoglevel(loggerName = "play", levelName = "foo")
        .left
        .value mustBe LevelNotFound("foo")
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
      Logger("ROOT", "ROOT", List.empty, underlying = classicLogger("ROOT")),
      Logger("play", "play", List.empty, underlying = classicLogger("play")),
      Logger("play.core", "play.core", List.empty, underlying = classicLogger("play.core")),
      Logger(
        "play.core.cookie",
        "play.core.cookie",
        List.empty,
        underlying = classicLogger("play.core.cookie")
      ),
      Logger(
        "play.core.http",
        "play.core.http",
        List.empty,
        underlying = classicLogger("play.core.http")
      )
    )
  }

}
