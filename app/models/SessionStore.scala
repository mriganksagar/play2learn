package models

import slick.jdbc.PostgresProfile.api._
import java.util.UUID

case class SessionEntity(sessionId: UUID, username: String)

class SessionStore(tag: Tag) extends Table[SessionEntity](tag, "session_store"){

    def sessionId = column[UUID]("session_id", O.PrimaryKey)
    def username = column[String]("username")
    def * = (sessionId, username) <> (SessionEntity.apply.tupled, SessionEntity.unapply)
}