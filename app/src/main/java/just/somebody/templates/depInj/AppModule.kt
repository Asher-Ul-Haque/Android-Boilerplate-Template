package just.somebody.templates.depInj

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStore
import androidx.datastore.dataStoreFile
import just.somebody.templates.data.Api
import just.somebody.templates.data.ApiImpl
import just.somebody.templates.data.AppSettings
import just.somebody.templates.data.RepositoryImpl
import just.somebody.templates.domain.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob


// - - - add the necessary components from data and domain
interface AppModuleInterface
{
  val api             : Api
  val repo            : Repository
  val settingsManager : SettingsManager
}

class AppModule(private val APP_CONTEXT : Context) : AppModuleInterface
{
  override val api             : Api         by lazy { ApiImpl(); }
  override val repo            : Repository  by lazy { RepositoryImpl(this.api);}
  override val settingsManager : SettingsManager by lazy { SettingsManager(appSettingsDataStore) }

  private val appSettingsDataStore : DataStore<AppSettings> by lazy ()
  {
    DataStoreFactory.create(
      serializer = AppSettingsSerializer,
      scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    ) { APP_CONTEXT.dataStoreFile("app-settings.json") }
  }

}