package just.somebody.templates

import android.app.Application
import just.somebody.templates.appModule.AppModule
import just.somebody.templates.appModule.AppModuleInterface

class App() : Application()
{
  companion object { lateinit var appModule : AppModuleInterface }

  override fun onCreate()
  {
    super.onCreate()
    appModule = AppModule(this)
  }
}