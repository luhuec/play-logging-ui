package unit

import controllers.MyLogger
import org.mockito.IdiomaticMockito
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import service.{LoggerRepo, LoggerService}

import scala.collection.mutable.ListBuffer

class LoggerServiceSpec extends AnyWordSpec with Matchers with IdiomaticMockito {

  "LoggerService" must {
    "foo" in new Fixture {
      service.getLoggers mustBe List(
        MyLogger(name = "ROOT", level = "ROOT", children = ListBuffer.empty),
        MyLogger(
          name = "play",
          level = "play",
          children = ListBuffer(
            MyLogger(
              name = "play.core",
              level = "play.core",
              children = ListBuffer(
                MyLogger(
                  name = "play.core.cookie",
                  level = "play.core.cookie",
                  children = ListBuffer.empty
                ),
                MyLogger(
                  name = "play.core.http",
                  level = "play.core.http",
                  children = ListBuffer.empty
                )
              )
            )
          )
        )
      )
    }
  }

  private trait Fixture {

    val repo: LoggerRepo = mock[LoggerRepo]

    repo.get returns List(
      MyLogger("ROOT", "ROOT", ListBuffer.empty),
      MyLogger("play", "play", ListBuffer.empty),
      MyLogger("play.core", "play.core", ListBuffer.empty),
      MyLogger("play.core.cookie", "play.core.cookie", ListBuffer.empty),
      MyLogger("play.core.http", "play.core.http", ListBuffer.empty)
    )

    val service = new LoggerService(repo)
  }

}
