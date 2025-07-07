package just.somebody.templates

import android.app.Activity
import android.app.Application
import just.somebody.templates.depInj.AppModule
import just.somebody.templates.depInj.AppModuleInterface

class App() : Application()
{
  companion object { lateinit var appModule : AppModuleInterface }

  override fun onCreate()
  {
    super.onCreate()
    appModule = AppModule(this)
  }
}