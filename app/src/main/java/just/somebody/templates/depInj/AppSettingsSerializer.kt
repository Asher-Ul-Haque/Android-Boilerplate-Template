package just.somebody.templates.depInj

import androidx.datastore.core.Serializer
import just.somebody.templates.data.AppSettings
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object AppSettingsSerializer : Serializer<AppSettings>
{
  override val defaultValue: AppSettings
    get() = AppSettings()

  override suspend fun readFrom(INPUT : InputStream) : AppSettings
  {
    return try
    {
      Json.decodeFromString(
        deserializer  = AppSettings.serializer(),
        string        = INPUT.readBytes().decodeToString()
      )
    }
    catch (e : SerializationException)
    {
      e.printStackTrace()
      defaultValue
    }
  }

  override suspend fun writeTo(APP_SETTINGS : AppSettings, OUTPUT : OutputStream)
  {
    OUTPUT.write(
      Json.encodeToString(
        serializer  = AppSettings.serializer(),
        value       = APP_SETTINGS
      ).encodeToByteArray()
    )
  }
}