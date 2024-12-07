package models.repository

import javax.inject.*
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.PostgresProfile

import scala.concurrent.{ExecutionContext, Future}
import models.tables.UserSession

import java.util.UUID

@Singleton
class SessionStoreDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) {
    val dbConfig = dbConfigProvider.get[PostgresProfile]
    import dbConfig._
    import profile.api._
    import models.tables.Tables.sessions

    def authorise(sessionId: UUID): Future[Boolean] =
        db.run(sessions.filter(_.sessionId === sessionId).exists.result)        
        
    def addSession(sessionId:UUID, username: String): Future[UserSession]=
        db.run((sessions returning sessions) += UserSession(sessionId, username))

    def removeSession(sessionId: UUID): Future[Int] =
        db.run(sessions.filter(_.sessionId === sessionId).delete)
}
