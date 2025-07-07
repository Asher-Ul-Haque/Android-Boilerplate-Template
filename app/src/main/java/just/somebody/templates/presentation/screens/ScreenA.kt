package just.somebody.templates.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import just.somebody.templates.App
import just.somebody.templates.presentation.effects.SnackbarController
import just.somebody.templates.presentation.effects.SnackbarEvent
import just.somebody.templates.presentation.viewModels.ScreenAViewModel
import kotlinx.coroutines.launch

@Composable
fun ScreenA(
  MODIFIER   : Modifier = Modifier,
  VIEW_MODEL : ScreenAViewModel)
{
  val scope = rememberCoroutineScope()
  Column (
    modifier            = MODIFIER
      .fillMaxSize()
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  )
  {
    Button(
      onClick =
        {
          scope.launch()
          {
            SnackbarController.sendEvent(
              EVENT = SnackbarEvent(message = "Hello!, world")
            )
          }
        }
    )
    { Text("Show Snackbar") }

    Button(onClick = { VIEW_MODEL.goAway() } )
    { Text("Show Snackbar wtih action") }

    Button(onClick = { VIEW_MODEL.showSnackbar() })
    { Text("Show Snackbar from viewModel") }

    val askPermission = remember { mutableStateOf(false) }
    Button(onClick = { askPermission.value = true})
    { Text("Ask Camera permission") }

    App.appModule.permissionManager.RequestPermissionIfNeeded(
      PERMISSION      = android.Manifest.permission.CAMERA,
      ON_GRANT         = { /* success */ },
      TRIGGER         = askPermission.value,
      ON_TRIGGER       = { askPermission.value = !askPermission.value },
      GO_TO_SETTINGS = true,
    )

    val isConnected by VIEW_MODEL.isConnected.collectAsState()
    Text("is Connected : ${isConnected.toString()}")
  }
}