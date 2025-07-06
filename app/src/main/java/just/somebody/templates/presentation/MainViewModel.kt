package just.somebody.templates.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import just.somebody.templates.data.AppSettings
import just.somebody.templates.depInj.SettingsManager
import just.somebody.templates.domain.Repository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
  private val REPO             : Repository,
  private val SETTINGS_MANAGER : SettingsManager
) : ViewModel()
{
  val appSettings : StateFlow<AppSettings> = SETTINGS_MANAGER.settingsFlow.stateIn(
    viewModelScope,
    SharingStarted.Lazily,
    AppSettings()
  )

  fun doSomething()
  {
    viewModelScope.launch { REPO.doSomething() }
  }

  fun updateSomething(SOMETHING : Int)
  {
    viewModelScope.launch ()
    {
      val current = appSettings.value
      SETTINGS_MANAGER.updateSettings(current.copy(something = SOMETHING))
    }
  }
}