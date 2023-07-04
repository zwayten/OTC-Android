package com.example.orangetrainingcenterandroid.presentation.poll

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.orangetrainingcenterandroid.R
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.*
import com.example.orangetrainingcenterandroid.domain.poll.model.PollDomainModel
import com.example.orangetrainingcenterandroid.presentation.home.HomeViewModel
import com.example.orangetrainingcenterandroid.ui.theme.Orange500


@Composable
fun PollFrame(poll: PollDomainModel, homeViewModel: HomeViewModel) {
    val hasVoted = remember { mutableStateOf(poll.hasVoted) }
    val surfaceColor: Color = MaterialTheme.colors.surface
    val textColor: Color = MaterialTheme.colors.onBackground
    val textStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = textColor,
        fontFamily = FontFamily(Font(R.font.neue_helvetica)),
        textAlign = TextAlign.Start
    )
    Card(
        modifier = Modifier.fillMaxWidth(1f),
        elevation = 8.dp,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(10.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                modifier = Modifier.padding(bottom = 10.dp),
                text = poll.title,
                style = textStyle
            )
            Text(
                text = poll.description,
                modifier = Modifier.padding(bottom = 16.dp),
                style = textStyle,
            )
            if (hasVoted.value) {
                poll.choices.forEach { choice ->
                    LoadingBarWithPercentage(
                        answer = choice.choice,
                        progress = choice.votes.toFloat(),
                        height = 3.dp,
                    )
                }
            }
            else {
                poll.choices.forEachIndexed { index, choice ->
                    VoteArea(choice.choice, index) { selectedIndex ->
                        homeViewModel.votePoll(poll._id,selectedIndex)
                        hasVoted.value = true
                    }
                }
            }

        }
    }
}

@Composable
fun LoadingBarWithPercentage(answer: String, progress: Float, height: Dp = 10.dp, animationDurationMillis: Int = 1000) {
    val animatedProgress = remember { Animatable(0f) }
    val animatedPercentage = animatedProgress.value

    LaunchedEffect(progress) {
        animatedProgress.animateTo(
            targetValue = progress,
            animationSpec = tween(durationMillis = animationDurationMillis)
        )
    }
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 4.dp)) {
        Text(
            text = answer,
            modifier = Modifier.padding(start = 10.dp),
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                textAlign = TextAlign.Start
            ),
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "${animatedPercentage.toInt()}%",
            style = TextStyle(color = Color.Black),
            textAlign = TextAlign.End,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 10.dp),) {
        Canvas(
            modifier = Modifier
                .weight(1f)
                .height(height)
        ) {
            val barWidth = (size.width * animatedProgress.value / 100)
            val barPath = Path().apply {
                moveTo(0f, size.height / 2)
                lineTo(barWidth, size.height / 2)
            }
            val gradient = Brush.linearGradient(
                colors = listOf(Orange500, Orange500),
                start = Offset(0f, 0f),
                end = Offset(size.width, 0f)
            )
            drawPath(
                path = barPath,
                brush = gradient,
                style = Stroke(width = size.height)
            )
        }

    }
}

@Composable
fun VoteArea(answer: String, index: Int, onClick: (Int) -> Unit) {
    Card(
        elevation = 4.dp,
        modifier = Modifier.padding(bottom = 4.dp).fillMaxWidth(1f).clickable { onClick(index) },
        contentColor = Orange500,

    ) {
        Row(modifier = Modifier.fillMaxWidth(1f),
            verticalAlignment = Alignment.CenterVertically) {
            val textStyle = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = MaterialTheme.colors.onSurface,
                fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                textAlign = TextAlign.Start
            )



            Text(
                text = answer,
                modifier = Modifier
                    .padding(start = 10.dp, bottom = 10.dp),
                style = textStyle
            )
        }
    }
}