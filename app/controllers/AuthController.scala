package controllers

import javax.inject._
import play.api.mvc._
import Auth.AuthService
import Auth.SessionAuthServiceImpl
import play.api.data.Form
import play.mvc.BodyParser.FormUrlEncoded
import play.core.parsers.FormUrlEncodedParser

@Singleton
class AuthController @Inject() (val controllerComponents: ControllerComponents)(val authService: SessionAuthServiceImpl) extends BaseController {


    //get request for login view page
    def login(redirect:Option[String]) = Action { implicit request: Request[AnyContent] =>
        // val redirectPath = request.queryString.get("redirect").map(_.head).getOrElse("/home")
        Ok(views.html.login(redirect))    
    }

    def signup() = Action { implicit request: Request[AnyContent] =>
        // Ok(views.html.signup())   
        Ok(views.html.signup())
    }

    // post reqeust for signing up
    def createUser() = Action { implicit request: Request[AnyContent] =>
        for{
            data <- request.body.asFormUrlEncoded
            username <- data.get("email")
            password <- data.get("password")
        } do authService.signup(username.head, password.head)
        
        Redirect(routes.AuthController.login(None))
    }

    // post request for loging in
    def authenticateUser(redirect: Option[String]) = Action { implicit request:Request[AnyContent] =>
        val sessionId = request.body.asFormUrlEncoded.flatMap(form => 
            for{
                username <- form.get("username")
                password <- form.get("password")
                sessionId <- authService.authenticate(username.head, password.head)
            } yield sessionId
        )
        
        sessionId.map(
            sid => Results.Redirect(redirect.getOrElse("/home")).withSession("sessionId" -> sid)
        ).getOrElse(Results.BadRequest)
    }
}
