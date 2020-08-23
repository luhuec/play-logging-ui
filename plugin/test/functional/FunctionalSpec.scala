package functional

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneServerPerTest
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.ws.WSClient

class FunctionalSpec extends PlaySpec with GuiceOneServerPerTest with ScalaFutures {

  override implicit val patienceConfig: PatienceConfig = PatienceConfig(timeout = Span(1, Seconds))

  override def fakeApplication(): Application = {
    GuiceApplicationBuilder()
      .configure("play.http.router" -> "playloggingui.Routes")
      .build()
  }

  "play-logging-ui" must {

    "return 200 response for /playloggingui" in {
      implicit val ws: WSClient = app.injector.instanceOf(classOf[WSClient])
      val response              = wsUrl("/playloggingui").get.futureValue

      response.status mustBe 200
      response.contentType mustBe "text/html; charset=UTF-8"
    }

    "return all loggers for /playloggingui/logger" in {
      implicit val ws: WSClient = app.injector.instanceOf(classOf[WSClient])
      val response              = wsUrl("/playloggingui/logger").get.futureValue

      response.status mustBe 200
      response.contentType mustBe "application/json"
    }

    "return 204 for /playloggingui/logger/update" in {
      implicit val ws: WSClient = app.injector.instanceOf(classOf[WSClient])
      val response              = wsUrl("/playloggingui/logger/update?logger=akka&level=info").get.futureValue

      response.status mustBe 204
    }

    "return 400 for /playloggingui/logger/update if logger not found" in {
      implicit val ws: WSClient = app.injector.instanceOf(classOf[WSClient])
      val response              = wsUrl("/playloggingui/logger/update?logger=foo&level=info").get.futureValue

      response.status mustBe 400
      response.body mustBe "Logger 'foo' not found"
    }

    "return 400 for /playloggingui/logger/update if level is invalid" in {
      implicit val ws: WSClient = app.injector.instanceOf(classOf[WSClient])
      val response              = wsUrl("/playloggingui/logger/update?logger=akka&level=foo").get.futureValue

      response.status mustBe 400
      response.body mustBe "Level 'foo' not found"
    }

  }

}
