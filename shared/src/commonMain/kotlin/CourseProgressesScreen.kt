import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import model.CourseProgresse

@Composable
fun CourseProgressesScreen(
    state: List<CourseProgresse>,
    onScroll: (Int) -> Unit = {}
) {
    if (state.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "Nenhum curso em progresso no momento",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxSize()
            )
        }
    } else {
        val brush = Brush.verticalGradient(listOf(Color.Blue.copy(0.2f), Color.Blue))

        val scrollState = rememberScrollState()

        LaunchedEffect(scrollState.value) {
            onScroll(scrollState.value)
        }
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .background(brush)
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
                Text(
                    "Cursos em progresso",
                    textAlign = TextAlign.Center
                )
            }
            val tempList = state + state + state
            tempList.forEach { courseProgresse ->
                CourseItem(courseProgresse)
            }
        }
    }
}

@Composable
fun CourseItem(course: CourseProgresse) {

//    data class CourseProgresse(
//        val finished: Boolean,
//        val id: Int,
//        val lastAccessTime: Long,
//        val name: String,
//        val progress: Int,
//        val readyToFinish: Boolean,
//        val slug: String
//    )


    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        /// image icon is https://www.alura.com.br/assets/api/cursos/SLUGHERE.svg
        Box(
            modifier = Modifier
                .weight(2f),
            contentAlignment = Alignment.Center
        ) {
            KamelImage(
                asyncPainterResource("https://www.alura.com.br/assets/api/cursos/${course.slug}.svg"),
                contentDescription = "Logo curso de ${course.name}",
                modifier = Modifier
                    .size(50.dp),
            )
        }
        Column(
            modifier = Modifier.weight(8f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = course.name,
            )
            Text("Progresso: ${course.progress}")
            Text("Último acesso: ${course.lastAccessTime.timestampToDisplayDate()}")
            Text("Pronto para finalizar: ${course.readyToFinish}")
        }
    }
}

fun Long.timestampToDisplayDate(): String {

    // coverting a data like "1629780000000" (alura api pattern) to "23/08/2021 às 16:00"
    val dateTime =
        Instant.fromEpochMilliseconds(this).toLocalDateTime(TimeZone.currentSystemDefault())
    return "${dateTime.dayOfMonth.toString().padStart(2, '0')}/${
        dateTime.monthNumber.toString().padStart(2, '0')
    }/${dateTime.year} às ${dateTime.hour.toString().padStart(2, '0')}:${
        dateTime.minute.toString().padStart(2, '0')
    }"
}
