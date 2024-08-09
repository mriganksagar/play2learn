package models

import slick.jdbc.PostgresProfile.api._

import scala.annotation.targetName

final case class Credential(id:Option[Long], username:String, passwordHash: String)

class CredentialTable(tag:Tag) extends Table[Credential](tag, "credential_store") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def username = column[String]("username")
    def passwordHash = column[String]("password_hash")
    def * = (id.?, username, passwordHash) <> (Credential.apply.tupled, Credential.unapply)
}

