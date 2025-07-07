import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.documentfile.provider.DocumentFile
import just.somebody.templates.appModule.storage.dataStore.DataStoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface ExternalStorageManager
{
  @Composable
  fun PickDirectory(
    KEY       : String,
    ON_PICKED : (Boolean) -> Unit
  )

  @Composable
  fun PickFile(
    MIME_TYPES : Array<String>,
    ON_PICKED  : (Uri?) -> Unit
  )

  suspend fun getDirectory(KEY : String): DocumentFile?

  suspend fun listFiles(
    KEY       : String,
    EXTENSION : String? = null
  ): List<DocumentFile>

  suspend fun saveFile(
    KEY       : String,
    FILE_NAME : String,
    CONTENT   : ByteArray
  ): Boolean

  suspend fun readFile(
    KEY       : String,
    FILE_NAME : String
  ): ByteArray?

  suspend fun deleteFile(
    KEY       : String,
    FILE_NAME : String
  ): Boolean
}


class DefaultExternalStorageManager(
  private val CONTEXT             : Context,
  private val DATA_STORE_MANAGER  : DataStoreManager,
) : ExternalStorageManager
{

  companion object
  {
    const val MIME_TEXT   = "text/plain"
    const val MIME_IMAGE  = "image/*"
    const val MIME_AUDIO  = "audio/*"
    const val MIME_VIDEO  = "video/*"
    const val MIME_PDF    = "application/pdf"
    const val MIME_BINARY = "application/octet-stream"
    const val MIME_JSON   = "application/json"
    const val MIME_ZIP    = "application/zip"
    const val MIME_ANY    = "*/*"
  }

  private val contentResolver get() = CONTEXT.contentResolver

  @Composable
  override fun PickDirectory(
    KEY       : String,
    ON_PICKED : (Boolean) -> Unit
  )
  {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply()
    {
      addFlags(
        Intent.FLAG_GRANT_READ_URI_PERMISSION or
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION or
            Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION or
            Intent.FLAG_GRANT_PREFIX_URI_PERMISSION
      )
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result ->
      val uri = result.data?.data
      if (uri != null)
      {
        contentResolver.takePersistableUriPermission(
          uri,
          Intent.FLAG_GRANT_READ_URI_PERMISSION or
              Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )

        CoroutineScope(Dispatchers.IO).launch()
        {
          val settings        = DATA_STORE_MANAGER.getSettings()
          val updatedSettings = settings.copy(externalUris = settings.externalUris + (KEY to uri.toString()))
          DATA_STORE_MANAGER.updateSettings(updatedSettings)
          ON_PICKED(true)
        }
      }
      else ON_PICKED(false)
    }

    launcher.launch(intent)
  }

  @Composable
  override fun PickFile(
    MIME_TYPES: Array<String>,
    ON_PICKED: (Uri?) -> Unit
  )
  {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply()
    {
      addCategory(Intent.CATEGORY_OPENABLE)
      type =
        if (MIME_TYPES.size == 1) MIME_TYPES[0]
        else                      "*/*"
      putExtra(Intent.EXTRA_MIME_TYPES, MIME_TYPES)
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult())
    { result -> ON_PICKED(result.data?.data) }

    launcher.launch(intent)
  }

  override suspend fun getDirectory(KEY : String): DocumentFile?
  {
    val uri = DATA_STORE_MANAGER.getSettings().externalUris[KEY] ?: return null
    return DocumentFile.fromTreeUri(CONTEXT, Uri.parse(uri))
  }

  override suspend fun listFiles(KEY : String, EXTENSION : String?) : List<DocumentFile>
  {
    return getDirectory(KEY)
      ?.listFiles()
      ?.filter { file ->
        EXTENSION == null || file.name?.endsWith(".$EXTENSION", ignoreCase = true) == true
      } ?: emptyList()
  }

  override suspend fun saveFile(
    KEY       : String,
    FILE_NAME : String,
    CONTENT   : ByteArray
  ): Boolean
  {
    val dir  = getDirectory(KEY) ?: return false
    val file = dir.createFile("application/octet-stream", FILE_NAME) ?: return false

    return try
    {
      contentResolver.openOutputStream(file.uri)?.use { it.write(CONTENT) } ?: return false
      true
    } catch (e: Exception) { false }
  }

  override suspend fun readFile(KEY : String, FILE_NAME : String): ByteArray?
  {
    val file = getDirectory(KEY)
      ?.listFiles()
      ?.firstOrNull { it.name == FILE_NAME } ?: return null

    return try                  { contentResolver.openInputStream(file.uri)?.use { it.readBytes() } }
           catch (e: Exception) { null }
  }

  override suspend fun deleteFile(KEY : String, FILE_NAME : String): Boolean
  {
    val file = getDirectory(KEY)
      ?.listFiles()
      ?.firstOrNull { it.name == FILE_NAME } ?: return false

    return file.delete()
  }
}
