package models.repository

import javax.inject._
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.PostgresProfile
import scala.concurrent.{ExecutionContext, Future }
import models.tables.{Credential, Tables}

/** 
*   play will inject 
*   @param dbConfigProvider
*/

@Singleton
class CredentialsStoreDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
    val dbConfig = dbConfigProvider.get[PostgresProfile]
    import dbConfig._
    import profile.api._
    import Tables.credentials

    def getByUsername(username: String): Future[Credential] = 
        db.run(Tables.credentials.filter(_.username === username).result.head)
    
    def insert(newCredential: Credential): Future[Long] =
        db.run((credentials returning credentials.map(_.id)) += newCredential)

    def update(id: Long, newCredential: Credential): Future[Int] =
        db.run(credentials.filter(_.id === id).update(newCredential))

    def delete(id: Long): Future[Int] =
        db.run(credentials.filter(_.id === id).delete)

}

