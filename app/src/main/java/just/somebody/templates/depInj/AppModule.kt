package just.somebody.templates.depInj

import android.content.Context
import just.somebody.templates.data.Api
import just.somebody.templates.data.ApiImpl
import just.somebody.templates.data.RepositoryImpl
import just.somebody.templates.domain.Repository


// - - - add the necessary components from data and domain
interface AppModuleInterface
{
  val api  : Api
  val repo : Repository
}

class AppModule(private val appContext : Context) : AppModuleInterface
{
  override val api  : Api         by lazy { ApiImpl(); }
  override val repo : Repository  by lazy { RepositoryImpl(this.api);}
}