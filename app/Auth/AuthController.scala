package Auth

import javax.inject._
import play.api.mvc._

@Singleton
class AuthController @Inject() (val controllerComponents: ControllerComponents)(val authService: AuthService) extends BaseController {
  
    def login() = Action { implicit request: Request[AnyContent] =>
        Ok(views.html.login())    
    }

    def logout() = ???

    def validateUser() = ???
}
