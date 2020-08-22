package controllers

import javax.inject.{Inject, _}
import play.api.mvc.{AbstractController, ControllerComponents}

@Singleton
class HomeController @Inject() (cc: ControllerComponents)(implicit
    assetsFinder: AssetsFinder
) extends AbstractController(cc) {

  def index =
    Action {
      Ok("Hello")
    }
}
