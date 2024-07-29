package Auth
import javax.inject.{Singleton}
import java.util.UUID
import play.api.mvc.Request
import play.api.mvc.AnyContent
import com.github.t3hnar.bcrypt.*
import scala.util.{Failure, Success, Try}

trait AuthService {
    def authorise(sessionId: String):Boolean
    def authenticate(username:String, password: String): Option[String]
    def signup(username:String, password: String): Try[Unit]
    def logout(sessionId:String): Boolean
}

@Singleton
class SessionAuthServiceImpl extends AuthService {
    def authorise(sessionId:String): Boolean = AuthStore.authoriseSession(sessionId)
    
    def authenticate(username:String, password: String): Option[String] = {
        println(s" auth impl login called with $username $password")
        for {
            storedHashed <- AuthStore.getUsersPassword(username)
            isCorrect <- password.isBcryptedSafeBounded(storedHashed).toOption
            sessionId = UUID.randomUUID().toString
            if AuthStore.addSession(sessionId)   
        } yield sessionId 
    }

    def signup(username:String, password: String): Try[Unit] = {
        println(s"signup auth impl called with username: $username and password: $password")
        for {
            hashedPassword <- password.bcryptSafeBounded
            _ <- AuthStore.addUser(username, hashedPassword)
        } yield ()
    }

    def logout(sessionId:String): Boolean = AuthStore.removeSession(sessionId)
}
