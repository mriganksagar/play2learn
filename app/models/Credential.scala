package models

case class Credential(id:Option[Long], username:String, passwordHash: String)