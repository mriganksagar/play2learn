package models

import slick.jdbc.PostgresProfile.api.*

import scala.annotation.targetName

class CredentialsStore(tag:Tag) extends Table[Credential](tag, "credentials_store") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def username = column[String]("username")
    def passwordHash = column[String]("password_hash")
    def * = (id.?, username, passwordHash) <> (Credential.apply.tupled, Credential.unapply)
}

