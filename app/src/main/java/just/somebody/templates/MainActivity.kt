package just.somebody.templates

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import just.somebody.templates.presentation.MainViewModel
import just.somebody.templates.presentation.viewModelFactory
import just.somebody.templates.ui.theme.TemplateTheme
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity()
{
  override fun onCreate(savedInstanceState: Bundle?)
  {
    super.onCreate(savedInstanceState)
    App().onCreate()
    enableEdgeToEdge()
    setContent {
      TemplateTheme {
        val viewModel = viewModel<MainViewModel>(
          factory = viewModelFactory { MainViewModel(App.appModule.repo) }
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