package Auth

import javax.inject.Inject
import play.api.mvc.BodyParsers
import play.api.mvc.ActionBuilderImpl
import scala.concurrent.{ExecutionContext, Future}
import play.api.mvc._
import controllers.routes


class AuthAction @Inject() (parser:BodyParsers.Default)(implicit ec: ExecutionContext) extends ActionBuilderImpl(parser) {
    import Authorisation._
    override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] = {
        val sessionId = request.session.get("sessionId")
        val isAuthorised = sessionId.map(sessionId => authoriseSession(sessionId)).getOrElse(false)
        if isAuthorised then block(request)
        else Future.successful(Results.Redirect(routes.HomeController.loginPage()))
    }   
}
