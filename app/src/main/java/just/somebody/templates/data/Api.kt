package just.somebody.templates.data

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
data class AppSettings
(
  val something   : Int                 = 0,
  val otherThings : PersistentList<Int> = persistentListOf()
)

interface Api
{
  fun         doSomething();
  suspend fun doSomethingElse();
}

class ApiImpl : Api
{
  override fun doSomething()
  { println("Something done") }

  override suspend fun doSomethingElse()
  { println("Something else done") }
}