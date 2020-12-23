package com.github.luhuec.playloggingui.controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

@Singleton
class AssetsController @Inject() (cc: ControllerComponents) extends AbstractController(cc) {

  def js(): Action[AnyContent] =
    Action {
      Ok.sendResource("assets/main.e6356a2e70d4245effa4.js")
    }

}
