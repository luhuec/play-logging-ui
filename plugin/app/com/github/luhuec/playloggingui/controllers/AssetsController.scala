package com.github.luhuec.playloggingui.controllers

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

@Singleton
class AssetsController @Inject() (cc: ControllerComponents) extends AbstractController(cc) {

  def js(): Action[AnyContent] =
    Action {
      Ok.sendResource("assets/main.79db02bbea437624490e.js")
    }

}
