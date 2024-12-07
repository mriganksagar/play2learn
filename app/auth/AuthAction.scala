package auth

import javax.inject.Inject
import play.api.mvc.BodyParsers
import play.api.mvc.ActionBuilderImpl
import scala.concurrent.{ExecutionContext, Future}
import play.api.mvc._
import controllers.routes
import play.api.mvc.ActionTransformer
import models.repository.SessionStoreDao
import java.util.UUID
import scala.util.Success
import scala.util.Failure


class AuthAction @Inject() (parser:BodyParsers.Default, sessionStoreDao: SessionStoreDao)(implicit ec: ExecutionContext) extends ActionBuilderImpl(parser) {
    override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] = {
        request.session.get("sessionId") match 
            case None => Future.successful(Results.Redirect(routes.AuthController.login(None)))
            case Some(sessionId) => sessionStoreDao.authorise(UUID.fromString(sessionId)).transformWith{
                case Success(value) => block(request)
                case Failure(exception) => Future.successful(Results.Redirect(routes.AuthController.login(None)))
            }
    }   
}
