import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import model.CourseProgresse
import model.Guide

@Composable
fun GuidesScreen(
    state: List<Guide>,
    onScroll: (Int) -> Unit = {}
) {

    if (state.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "Nenhum guia em progresso no momento",
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
                    "Guias em progresso",
                    textAlign = TextAlign.Center
                )
            }
            val tempList = state + state + state
            tempList.forEach { courseProgresse ->
                GuideItem(courseProgresse)
            }
        }
    }
}

@Composable
fun GuideItem(guide: Guide) {
//    @Serializable
//    data class Guide(
//        val author: String,
//        val code: String,
//        val color: String,
//        val finishedCourses: Int,
//        val finishedSteps: Int,
//        val id: Int,
//        val kind: String,
//        val lastAccessTime: Long,
//        val name: String,
//        val totalCourses: Int,
//        val totalSteps: Int,
//        val url: String
//    )


    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = guide.name)
        Text(text = guide.author)
        Text(text = guide.code)
        Text(text = guide.color)
        Text(text = guide.finishedCourses.toString())
        Text(text = guide.finishedSteps.toString())
        Text(text = guide.kind)
        Text(text = guide.lastAccessTime.toString())
        Text(text = guide.totalCourses.toString())
        Text(text = guide.totalSteps.toString())
        Text(text = guide.url)
    }
}