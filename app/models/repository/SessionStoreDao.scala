package models.repository

import javax.inject.*
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.PostgresProfile

import scala.concurrent.{ExecutionContext, Future}
import models.{SessionTable, SessionEntity}

import java.util.UUID

@Singleton
class SessionStoreDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) {
    val dbConfig = dbConfigProvider.get[PostgresProfile]
    import dbConfig._
    import profile.api._
      
    val xx: TableQuery[SessionTable] = TableQuery[SessionTable]
    xx
    val sessionStore = TableQuery[SessionTable]
    def authorise(sessionId: UUID): Future[Boolean] =
        db.run(sessionStore.filter(_.sessionId === sessionId).exists.result)        
        
    def addSession(sessionId:UUID, username: String): Future[SessionEntity]=
        db.run((sessionStore returning sessionStore) += SessionEntity(sessionId, username))

    def removeSession(sessionId: UUID): Future[Int] =
        db.run(sessionStore.filter(_.sessionId === sessionId).delete)
}
