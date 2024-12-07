import com.google.inject.name.Names
import com.google.inject.AbstractModule

class Module extends AbstractModule {
  override def configure() = {
    bind(classOf[app.DatabaseInitialiser]).asEagerSingleton()
  }
}
