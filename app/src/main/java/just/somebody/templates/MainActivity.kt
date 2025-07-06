package just.somebody.templates

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.dataStore
import just.somebody.templates.presentation.MainViewModel
import just.somebody.templates.presentation.viewModelFactory
import just.somebody.templates.ui.theme.TemplateTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import just.somebody.templates.data.AppSettings
import just.somebody.templates.depInj.AppSettingsSerializer
import just.somebody.templates.presentation.SplashViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity()
{
  override fun onCreate(savedInstanceState: Bundle?)
  {
    super.onCreate(savedInstanceState)
    App().onCreate()

    val splashViewModel by viewModels<SplashViewModel>()

    installSplashScreen().apply ()
    {
      setKeepOnScreenCondition   { !splashViewModel.isReady.value }
      setOnExitAnimationListener ()
      { splash ->
        val zoomX = ObjectAnimator.ofFloat(
          splash.iconView,
          View.SCALE_X,
          1.0f,
          0.0f
        )
        zoomX.interpolator  = OvershootInterpolator()
        zoomX.duration      = 500L
        zoomX.doOnEnd { splash.remove() }

        val zoomY = ObjectAnimator.ofFloat(
          splash.iconView,
          View.SCALE_Y,
          1.0f,
          0.0f
        )
        zoomY.interpolator  = OvershootInterpolator()
        zoomY.duration      = 500L
        zoomY.doOnEnd { splash.remove() }

        zoomX.start()
        zoomY.start()
      }
    }

    enableEdgeToEdge()
    setContent {

      TemplateTheme {
        val viewModel = viewModel<MainViewModel>(
          factory = viewModelFactory ()
          { MainViewModel(
            App.appModule.repo,
            App.appModule.settingsManager)
          }
        )
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          Greeting(
            name = "Android",
            modifier = Modifier.padding(innerPadding)
          )
          Button(onClick = {viewModel.doSomething()})
          { Text(text = "Login") }
        }
      }
    }
  }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier)
{
  Text(
    text = "Hello $name!",
    modifier = modifier
  )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview()
{
  TemplateTheme {
    Greeting("Android")
  }
}