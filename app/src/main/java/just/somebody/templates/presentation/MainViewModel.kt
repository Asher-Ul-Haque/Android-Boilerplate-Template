package just.somebody.templates.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import just.somebody.templates.domain.Repository
import kotlinx.coroutines.launch

class MainViewModel(private val REPO : Repository) : ViewModel()
{
  fun doSomething()
  {
    viewModelScope.launch { REPO.doSomething() }
  }
}