package ui.tabs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import extensions.timestampToDisplayDate
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.http.Url
import model.CourseProgresse

@Composable
fun CourseProgressesScreen(
    state: List<CourseProgresse>,
    onScroll: (Int) -> Unit = {},
    animatedMainRotate: Float
) {
    val gradientBackgroundColor =
        Brush.verticalGradient(listOf(Color.Blue.copy(0.4f), Color(1, 8, 14)))

    AnimatedVisibility(
        state.isEmpty(),
        exit = fadeOut()
    ) {
        Column(
            modifier = Modifier
                .background(gradientBackgroundColor)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Nenhum curso em progresso no momento",
                textAlign = TextAlign.Center,
                color = Color.White,
                fontSize = 16.sp,
            )
        }
    }
    AnimatedVisibility(
        state.isNotEmpty(),
        enter = fadeIn()
    ) {
        val scrollState = rememberScrollState()

        LaunchedEffect(scrollState.value) {
            onScroll(scrollState.value)
        }
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .background(gradientBackgroundColor)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .background(Color(2, 17, 24).copy(0.2f), shape = CircleShape)
                        .border(1.dp, Color.Black.copy(0.5f), shape = CircleShape)
                        .padding(16.dp),
                ) {
                    Text(
                        "Cursos em progresso",
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        fontSize = 16.sp,
                    )
                }
            }

            state.forEach { courseProgresse ->
                CourseItem(
                    course = courseProgresse,
                    modifier = Modifier
                        .rotate(animatedMainRotate)
                )
            }
        }
    }
}

@Composable
fun CourseItem(modifier: Modifier, course: CourseProgresse) {

    val linearColors = Brush.linearGradient(
        listOf(
            Color.Gray.copy(0.2f),
            Color.White.copy(0.1f),
            Color.Gray.copy(0.2f)
        )
    )

    Box(
        modifier = modifier
            .height(IntrinsicSize.Max)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .background(linearColors, shape = RoundedCornerShape(16.dp))
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.height(8.dp))
        }

        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(2f),
                contentAlignment = Alignment.Center
            ) {
                KamelImage(
                    asyncPainterResource(Url("https://www.alura.com.br/assets/api/cursos/${course.slug}.svg")),
                    contentDescription = "Logo curso ${course.name}",
                    modifier = Modifier
                        .size(50.dp),
                )
            }
            Column(
                modifier = Modifier
                    .weight(8f)
                    .padding(top = 8.dp, bottom = 8.dp, end = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = course.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )

                Text(
                    "Progresso: ${course.progress}%",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
                Text(
                    "Último acesso: ${course.lastAccessTime.timestampToDisplayDate()}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            }
        }
    }
}


