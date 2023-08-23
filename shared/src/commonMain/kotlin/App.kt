import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import ui.home.HomeScreen
import ui.home.LoginScreen
import ui.home.ViewModel


@Composable
fun App(animatedMainRotate: Float = 0f) {
    MaterialTheme {
        val viewModel = getViewModel(Unit, viewModelFactory { ViewModel() })
        val state by viewModel.profileUiState.collectAsState()

        if (state.apiUrl.isNotEmpty()) {
            HomeScreen(viewModel, animatedMainRotate = animatedMainRotate)
        } else {
            LoginScreen(onLogin = { apiUrl ->
                viewModel.updateApiUrl(apiUrl)
            })
        }
    }
}


expect fun getPlatformName(): String