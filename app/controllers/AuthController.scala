package controllers

import javax.inject.*
import play.api.mvc.*
import Auth.AuthService
import Auth.SessionAuthServiceImpl
import play.api.data.Form
import play.mvc.BodyParser.FormUrlEncoded
import play.core.parsers.FormUrlEncodedParser

import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class AuthController @Inject() (val controllerComponents: ControllerComponents)(val authService: SessionAuthServiceImpl) extends BaseController {


    //get request for login view page
    def login(redirect:Option[String]): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
        Ok(views.html.login(redirect))    
    }

    def signup(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
        Ok(views.html.signup())
    }

    // post request for signing up
    def createUser() = Action.async { implicit request: Request[AnyContent] =>
        {for {
            data <- request.body.asFormUrlEncoded
            username <- data.get("email")
            password <- data.get("password")
        } yield {
            authService.signup(username.head, password.head)
              .map(_ => Redirect(routes.AuthController.login(None)))
              .recover { case _ => Results.NotImplemented("Error creating user") }
        }}.getOrElse(Future.successful(Results.BadRequest("invalid form of data provided")))
    }

    // post request for loging in
    def authenticateUser(redirect: Option[String]) = Action.async { implicit request:Request[AnyContent] => 
        {for {
            form <- request.body.asFormUrlEncoded
            username <- form.get("username")
            password <- form.get("password")
        } yield authService.authenticate(username.head, password.head)
          .map(_ => Redirect(redirect.getOrElse("/home")))
          .recover { case _ => Results.NotFound("User not found in the records")
        }}.getOrElse(Future.successful(Results.BadRequest("invalid form of data provided")))
    }
}
