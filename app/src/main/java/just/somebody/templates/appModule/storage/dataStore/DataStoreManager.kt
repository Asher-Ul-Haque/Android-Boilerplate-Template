package just.somebody.templates.appModule.storage.dataStore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class DataStoreManager(private val DATASTORE : DataStore<AppSettings>)
{
  val settingsFlow : Flow<AppSettings> = DATASTORE.data

  suspend fun updateSettings(NEW_SETTINGS : AppSettings)
  {
    DATASTORE.updateData ()
    { it.copy(
      something   = NEW_SETTINGS.something,
      otherThings = NEW_SETTINGS.otherThings)
    }
  }

  suspend fun clearSettings()
  { DATASTORE.updateData { AppSettings() } }

  suspend fun getSettings() : AppSettings
  { return settingsFlow.first() }
}