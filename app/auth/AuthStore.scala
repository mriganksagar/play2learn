package auth

import play.api.mvc.RequestHeader
import scala.util.{Try, Success}

object AuthStore{

    val sessionStore = scala.collection.mutable.Set.empty[String]
    val passwordStore = scala.collection.mutable.Map.empty[String, String]

    def authenticateUser(username:String, password: String): Boolean = {
        println(s"authstore impl authenticate called with $username $password")
        passwordStore.contains(username) && passwordStore(username) == password
    }

    def addUser(username:String, password: String): Try[Unit] = {
        println(s"signup auth store called with $username $password")
        passwordStore += username -> password
        Success(())
    }

    def getUsersPassword(username:String): Option[String] = passwordStore.get(username)

    def removeUser(username:String): Option[String] = passwordStore.remove(username)        


    def addSession(sessionId: String): Boolean = sessionStore.add(sessionId)

    def removeSession(sessionId: String): Boolean = sessionStore.remove(sessionId)

    def authoriseSession(sessionId:String): Boolean =  sessionStore.contains(sessionId)

}
