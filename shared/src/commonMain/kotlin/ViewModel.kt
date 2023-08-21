import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.Profile

data class UiState(
    val profile: Profile,
)

class ViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UiState(profile = Profile()))
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        updateProfile()
    }

    override fun onCleared() {
        httpClient.close()
    }

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    private fun updateProfile() {
        viewModelScope.launch {
            val profile = getProfile()
            _uiState.update {
                it.copy(profile = profile)
            }
        }
    }

    private suspend fun getProfile(): Profile {
        val profile = httpClient
            .get("https://www.alura.com.br/api/dashboard/682f477ae8b1c348c0c5a53cbd94f7def5c8fb260b2028da7c0fa1a8618c75ee")
            .body<Profile>()
        return profile
    }
}