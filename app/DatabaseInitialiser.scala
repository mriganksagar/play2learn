package app

import play.api.db.slick.DatabaseConfigProvider
import javax.inject._
import scala.concurrent.ExecutionContext
import javax.annotation._
import play.api.inject.ApplicationLifecycle
import models.tables.Tables
import slick.jdbc.PostgresProfile
import scala.util.Success
import scala.util.Failure

@Singleton
class DatabaseInitialiser @Inject() (val dbConfigProvider: DatabaseConfigProvider, val lifecycle: ApplicationLifecycle )(implicit ec: ExecutionContext) {
    val dbConfig = dbConfigProvider.get[PostgresProfile]
    import dbConfig._
    import profile.api._

    initialise()

    def initialise(): Unit = {
        println("Initialising DB initialiser")
        db.run{Tables.credentials.schema.createIfNotExists}
            .onComplete{
                case Success(value) => println(s"successfully created credentials table")
                case Failure(exception) => println(s"failed while creating credentials with ${exception.getMessage()}")
            }
        db.run{Tables.sessions.schema.createIfNotExists}
            .onComplete{
                case Success(value) => println(s"successfully created sessions table")
                case Failure(exception) => println(s"failed while creating sessions with ${exception.getMessage()}")
            }
    }   
}
