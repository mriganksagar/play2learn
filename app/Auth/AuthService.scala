package Auth


trait AuthService {
    def authorise(sessionId: String):Boolean
    def authenticate(sessionId: String): Boolean
}

class AuthServiceImpl extends AuthService {
    def authorise(sessionId: String): Boolean = ???
    def authenticate(sessionId:String): Boolean = ???
}
