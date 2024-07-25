package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import Auth.AuthAction

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents)(val authAction: AuthAction) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def hello(name: String) = authAction { implicit request: Request[AnyContent] =>
    Ok(s"hello, $name")
  }

  def loginPage() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.login())
  }

  def validateUser() = Action { implicit request: Request[AnyContent] =>
    Ok("validated")
  }
}
