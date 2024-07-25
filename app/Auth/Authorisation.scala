package Auth

import play.api.mvc.RequestHeader

object Authorisation {

    val sessionStore = scala.collection.mutable.Set.empty[String]
    
    def authenticate(request: RequestHeader) = {
        ???
    }
    
    def authoriseSession(sessionId:String): Boolean =  sessionStore.contains(sessionId)

}
