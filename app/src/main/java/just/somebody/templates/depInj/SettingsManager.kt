package just.somebody.templates.depInj

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import just.somebody.templates.data.AppSettings
import kotlinx.coroutines.flow.Flow

class SettingsManager(private val DATASTORE : DataStore<AppSettings>)
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

  @Composable
  fun getSettings() : AppSettings
  { return DATASTORE.data.collectAsState(AppSettings()).value }
}