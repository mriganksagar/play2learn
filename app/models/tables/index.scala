package models.tables

import slick.lifted.TableQuery

object Tables{
    val credentials = TableQuery[Credentials]
    val sessions = TableQuery[UserSessions]
}