package models.repository

import javax.inject._
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.PostgresProfile
import models.{Credential, CredentialTable}
import scala.concurrent.{ExecutionContext, Future }


/** 
*   play will inject 
*   @param dbConfigProvider
*/

@Singleton
class CredentialsStoreDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
    val dbConfig = dbConfigProvider.get[PostgresProfile]
    import dbConfig._
    import profile.api._

    private lazy val credentialsStore = TableQuery[CredentialTable]

    def getByUsername(username: String): Future[Credential] = 
        db.run(credentialsStore.filter(_.username === username).result.head)
    
    def insert(newCredential: Credential): Future[Long] =
        db.run((credentialsStore returning credentialsStore.map(_.id)) += newCredential)

    def update(id: Long, newCredential: Credential): Future[Int] =
        db.run(credentialsStore.filter(_.id === id).update(newCredential))

    def delete(id: Long): Future[Int] =
        db.run(credentialsStore.filter(_.id === id).delete)

}

