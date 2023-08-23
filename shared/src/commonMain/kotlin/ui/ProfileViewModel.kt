package ui

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

class ViewModel : ViewModel() {
    private val _profileUiState = MutableStateFlow(ProfileUiState(profile = Profile()))
    val profileUiState: StateFlow<ProfileUiState> = _profileUiState.asStateFlow()

    override fun onCleared() {
        httpClient.close()
    }

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    fun updateProfile() {
        viewModelScope.launch {
            val profile = getProfile()
            _profileUiState.update {
                it.copy(profile = profile)
            }
        }
    }

    private suspend fun getProfile(): Profile {
        val profile = httpClient
//            .get("https://www.alura.com.br/api/dashboard/682f477ae8b1c348c0c5a53cbd94f7def5c8fb260b2028da7c0fa1a8618c75ee")
            .get(profileUiState.value.apiUrl)
            .body<Profile>()
        return profile
    }

    fun updateApiUrl(url: String) {
        _profileUiState.update {
            it.copy(apiUrl = url)
        }
        updateProfile()
    }
}

data class ProfileUiState(
    val profile: Profile,
    val apiUrl: String = ""
)
