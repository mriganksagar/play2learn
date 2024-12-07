package models.tables

import slick.jdbc.PostgresProfile.api._
import java.util.UUID

final case class UserSession(sessionId: UUID, username: String)

class UserSessions(tag: Tag) extends Table[UserSession](tag, "session_store"){

    def sessionId = column[UUID]("session_id", O.PrimaryKey)
    def username = column[String]("username")
    def * = (sessionId, username).mapTo[UserSession]
}
