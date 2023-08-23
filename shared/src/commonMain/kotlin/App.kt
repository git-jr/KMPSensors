import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import extensions.noRippleClickable
import ui.HomeScreen
import ui.ViewModel


@Composable
fun App() {
    MaterialTheme {
        val viewModel = getViewModel(Unit, viewModelFactory { ViewModel() })
        val state by viewModel.profileUiState.collectAsState()

        if (state.apiUrl.isNotEmpty()) {
            HomeScreen(viewModel)
        } else {

            val gradientBackgroundColor =
                Brush.verticalGradient(listOf(Color(121, 103, 255), Color(1, 8, 11)))

            Box(
                Modifier.background(gradientBackgroundColor),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                ) {

                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        var urlTextState by remember { mutableStateOf("") }
                        Text(
                            text = "Digite a url da API do seu perfil ou use a de teste",
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                        )

                        Spacer(modifier = Modifier.padding(32.dp))

                        OutlinedTextField(
                            value = urlTextState,
                            onValueChange = { urlTextState = it },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.White,
                                cursorColor = Color.White,
                                textColor = Color.White,
                            ),
                        )


                        Spacer(modifier = Modifier.padding(24.dp))

                        Row(
                            modifier = Modifier
                                .background(
                                    Color(2, 17, 24).copy(0.2f),
                                    shape = RoundedCornerShape(20)
                                )
                                .border(
                                    1.dp,
                                    Color.Black.copy(0.5f),
                                    shape = RoundedCornerShape(20)
                                )
                                .padding(16.dp)
                                .fillMaxWidth(),
                        ) {
                            Text(
                                text = "Mergulhar",
                                textAlign = TextAlign.Center,
                                color = Color.White,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .noRippleClickable {
                                        if (urlTextState.isNotEmpty())
                                            viewModel.updateApiUrl(urlTextState)
                                    }
                            )
                        }

                        Spacer(modifier = Modifier.padding(8.dp))

                        TextButton(
                            onClick = { viewModel.updateApiUrl("https://git-jr.github.io/mock-apis/alura-api.json") },
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Usar url de teste",
                                textAlign = TextAlign.Center,
                                color = Color.White,
                            )
                        }
                    }
                }
            }
        }
    }
}

expect fun getPlatformName(): String