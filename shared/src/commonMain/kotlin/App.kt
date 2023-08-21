import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import model.AluraAPI
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource


private var horizontalPagerInAction by mutableStateOf(false)
private var valueToBeChangedOnTheLeft by mutableStateOf(0f)
private var valueToBeChangedOnTheRight by mutableStateOf(0f)
private var rotate by mutableStateOf(0)

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {


    LaunchedEffect(Unit) {
        val profile = getProfile()
        println("Olha aqui oh: $profile")
    }

    LaunchedEffect(Unit) {
        if (getPlatformName() == "ios") {
            valueToBeChangedOnTheLeft = 10f
            valueToBeChangedOnTheRight = 10f
        } else {
            valueToBeChangedOnTheLeft = 6f
            valueToBeChangedOnTheRight = 6f
        }
    }
    val scope = rememberCoroutineScope()

    MaterialTheme {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {


            // tabs
            TabScreen(
                modifier = Modifier,
                hasMovedToLeft = {
                    horizontalPagerInAction = true
                    scope.launch {
                        val originalValeuLeft = valueToBeChangedOnTheLeft
                        val targetValeuLeft = originalValeuLeft * 4f

                        valueToBeChangedOnTheLeft = targetValeuLeft
                        delay(700)
                        valueToBeChangedOnTheLeft = originalValeuLeft
                        delay(700)
                        horizontalPagerInAction = false
                    }
                    scope.launch {
                        val originalValeuRight = valueToBeChangedOnTheRight
                        val targetValeuRight = originalValeuRight / 2f

                        valueToBeChangedOnTheRight = targetValeuRight
                        delay(700)
                        valueToBeChangedOnTheRight = originalValeuRight
                        delay(700)
                        horizontalPagerInAction = false
                    }
                },

                hasMovedToRight = {
                    horizontalPagerInAction = true
                    scope.launch {
                        val originalValeuLeft = valueToBeChangedOnTheLeft
                        val targetValeuLeft = originalValeuLeft / 2f

                        valueToBeChangedOnTheLeft = targetValeuLeft
                        delay(700)
                        valueToBeChangedOnTheLeft = originalValeuLeft
                        delay(700)
                        horizontalPagerInAction = false
                    }
                    scope.launch {
                        val originalValeuRight = valueToBeChangedOnTheRight
                        val targetValeuRight = originalValeuRight * 4f

                        valueToBeChangedOnTheRight = targetValeuRight
                        delay(700)
                        valueToBeChangedOnTheRight = originalValeuRight
                        delay(700)
                        horizontalPagerInAction = false
                    }
                }
            )

            // wave
            SimpleWave(
                modifier = Modifier
                    .alpha(0.5f)
            )


            // image
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var greetingText by remember { mutableStateOf("Hello, World!") }
                var showImage by remember { mutableStateOf(false) }


                Button(onClick = {
                    greetingText = "Hello, ${getPlatformName()}"
                    showImage = !showImage
                }) {
                    Text(greetingText)
                }
                AnimatedVisibility(
                    enter = fadeIn() + expandVertically(
                        spring(
                            dampingRatio = Spring.DampingRatioHighBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ),
                    visible = showImage
                ) {
                    KamelImage(
                        asyncPainterResource("https://liquipedia.net/commons/images/2/2e/Going_Merry_Cartoon.png"),
                        null
                    )
                }
            }


        }
    }
}

@Composable
private fun SimpleWave(modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray),
            contentAlignment = Alignment.BottomCenter
        ) {
            var width: Float
            var height = 1080f

            val animatedOffset by animateFloatAsState(
                targetValue = rotate.toFloat(),
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioHighBouncy,
                    stiffness = Spring.StiffnessHigh
                )
            )


            val startHeight by animateFloatAsState(
                targetValue = height / valueToBeChangedOnTheLeft,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioHighBouncy,
                    stiffness = Spring.StiffnessVeryLow
                )
            )

            val endHeight by animateFloatAsState(
                targetValue = height / valueToBeChangedOnTheRight,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioHighBouncy,
                    stiffness = Spring.StiffnessVeryLow
                )
            )

            Canvas(
                modifier = Modifier
                    .size(500.dp)
                    .rotate(animatedOffset)
            ) {
                width = size.width
                height = size.height

                val peakHeight = height * 0.2f
                val dipHeight = height * 0.2f

                val path = Path().apply {
                    moveTo(-50f, startHeight + 100)  // Começa no canto esquerdo
                    cubicTo(
                        width * 0.33f, peakHeight,  // Primeiro ponto de controle
                        width * 0.66f, dipHeight,  // Segundo ponto de controle
                        width + 50, endHeight + 100  // Termina no canto direito
                    )
                    lineTo(width + 50, height)  // Vai para a borda inferior direita
                    lineTo(-50f, height)  // Vai para a borda inferior esquerda
                    close()  // Fecha o Path, voltando ao ponto inicial
                }

                //                            drawPath(path, color = Color(255, 0, 0), style = Stroke(width = 50f))

                drawPath(path, color = Color(0, 30, 255, 255), style = Fill)

            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun AppOld() {
    MaterialTheme {
        var greetingText by remember { mutableStateOf("Hello, World!") }
        var showImage by remember { mutableStateOf(false) }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                greetingText = "Hello, ${getPlatformName()}"
                showImage = !showImage
            }) {
                Text(greetingText)
            }
            AnimatedVisibility(
                enter = fadeIn() + expandVertically(
                    spring(
                        dampingRatio = Spring.DampingRatioHighBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ),
                visible = showImage
            ) {
                Image(
                    painterResource("compose-multiplatform.xml"),
                    null
                )
            }
        }
    }
}

val httpClient = HttpClient {
    install(ContentNegotiation) {
        json()
    }
}

suspend fun getProfile(): AluraAPI {
    val profile = httpClient
        .get("https://www.alura.com.br/api/dashboard/682f477ae8b1c348c0c5a53cbd94f7def5c8fb260b2028da7c0fa1a8618c75ee")
        .body<AluraAPI>()
    return profile
}

expect fun getPlatformName(): String