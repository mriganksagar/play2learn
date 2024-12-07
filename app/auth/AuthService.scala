package auth
import javax.inject.Singleton
import java.util.UUID
import play.api.mvc.Request
import play.api.mvc.AnyContent
import com.github.t3hnar.bcrypt.*

import scala.util.{Failure, Success, Try}
import models.repository.{CredentialsStoreDao, SessionStoreDao}

import javax.inject.*
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import AuthStore.sessionStore
import models.tables.Credential

trait AuthService {
    def authorise(sessionId: String):Future[Boolean]
    def authenticate(username:String, password: String): Future[String]
    def signup(username:String, password: String): Future[Unit]
    def logout(sessionId:String): Future[Boolean]
}

@Singleton
class SessionAuthServiceImpl @Inject() (credentialsStoreDao: CredentialsStoreDao, sessionStoreDao: SessionStoreDao ) extends AuthService {
    def authorise(sessionId:String): Future[Boolean] = sessionStoreDao.authorise(UUID.fromString(sessionId))

    def authenticate(username:String, password: String): Future[String] = {
        println(s" auth impl login called with $username $password")
        for {
            credential <- credentialsStoreDao.getByUsername(username)
            isCorrect <- Future.fromTry(password.isBcryptedSafeBounded(credential.passwordHash))
            if isCorrect
            session <- sessionStoreDao.addSession(UUID.randomUUID(), credential.username)
        } yield session.sessionId.toString
    }

    def signup(username:String, password: String): Future[Unit] = {
        println(s"signup auth impl called with username: $username and password: $password")
        for {
            hashedPassword <- Future.fromTry(password.bcryptSafeBounded)
            _ <- credentialsStoreDao.insert(Credential(None, username, hashedPassword)).andThen{case Failure(exception) => println(s"insert credentials failed with exception, ${exception.getMessage()}")}
        } yield ()
    }

    def logout(sessionId:String): Future[Boolean] =
        sessionStoreDao.removeSession(UUID.fromString(sessionId)).map(_>0)
}
