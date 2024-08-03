package Auth
import javax.inject.Singleton
import java.util.UUID
import play.api.mvc.Request
import play.api.mvc.AnyContent
import com.github.t3hnar.bcrypt.*

import scala.util.{Failure, Success, Try}
import repository.{CredentialsStoreDao, SessionStoreDao}

import javax.inject.*
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import Auth.AuthStore.sessionStore
import models.Credential
// import Auth.AuthStore.sessionStore

trait AuthService {
    def authorise(sessionId: String):Boolean
    def authenticate(username:String, password: String): Option[String]
    def signup(username:String, password: String): Try[Unit]
    def logout(sessionId:String): Boolean
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
            _ <- credentialsStoreDao.insert(Credential(None, username, hashedPassword))
        } yield ()
    }

    def logout(sessionId:String): Future[Boolean] =
        sessionStoreDao.removeSession(UUID.fromString(sessionId)).map(_>0)
}
