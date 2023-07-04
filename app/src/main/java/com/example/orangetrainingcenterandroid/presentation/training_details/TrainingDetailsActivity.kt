package com.example.orangetrainingcenterandroid.presentation.training_details


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.orangetrainingcenterandroid.R
import com.example.orangetrainingcenterandroid.common.BASE_URL2
import com.example.orangetrainingcenterandroid.domain.training.model.Planing
import com.example.orangetrainingcenterandroid.domain.training.model.TrainerProfileDomainModel
import com.example.orangetrainingcenterandroid.ui.theme.Grey
import com.example.orangetrainingcenterandroid.ui.theme.Grey2
import com.example.orangetrainingcenterandroid.ui.theme.Orange500
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TrainingDetailsScreen(trainingDetailsViewModel: TrainingDetailsViewModel, trainingId:String, navController: NavController) {
    println("details screen")
    trainingDetailsViewModel.verifyParticipation(trainingId)
    trainingDetailsViewModel.fetchTrainingDetail(trainingId)

    val trainingState = trainingDetailsViewModel.trainingState.value
    if (trainingState != null) {
        trainingDetailsViewModel.fetchTrainerProfile(trainingState.assignedTo._id)
    }
    if (trainingState != null) {
        if(trainingState.isQuizzEvaluation == true){
            trainingState.quizzEvaluation?.let { trainingDetailsViewModel.verifyHasVoted(it) }
        }
    }
    val hasVoted by trainingDetailsViewModel.hasVoted.collectAsState()





    val trainer = trainingDetailsViewModel.trainerProfile.value


    val customTextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        fontFamily = FontFamily(Font(R.font.neue_helvetica)),
        textAlign = TextAlign.Start
    )

    val isExpanded = remember { mutableStateOf(false) }
    var showProfileDialog by remember { mutableStateOf(false) }

    if (trainer != null) {
        DialogWithCloseIcon(showDialog = showProfileDialog ,onDismiss = { showProfileDialog = false },trainer.fullName) {
                TrainerProfile(trainer)
        }
    }



    if (trainingState != null) {


        val imageUrlValue= BASE_URL2 + trainingState.cover

        val formated_startDate = trainingDetailsViewModel.changeDateFormat(trainingState.startDate,"MMM dd yyyy")
        val formated_endDate = trainingDetailsViewModel.changeDateFormat(trainingState.endDate,"MMM dd yyyy")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        )  {
            ImageFromURL("${BASE_URL2}${trainingState.cover}")



            FlowRow(modifier = Modifier.padding(start = 20.dp, top = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween) {
                RoundedHashButton(
                    text = "# ${trainingState.theme.name}",
                    onClick = { /* Handle button click */ },
                    modifier = Modifier.weight(1f)
                )

                if(trainingState.onLine){
                    ModalityRoundedButton(
                        text = "Online",
                        backgroundColor = Color.Black,
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(start = 5.dp))
                }
                if(trainingState.isQuizzPreEvaluation == true){
                    IsQuizz(
                        text = "Pre-test",
                        backgroundColor = Color.Black,
                        modifier = Modifier.padding(start = 5.dp))
                }
                if(trainingState.isQuizzEvaluation == true){
                    IsQuizz(
                        text = "Final test",
                        backgroundColor = Color.Black,
                        modifier = Modifier.padding(start = 5.dp))
                }

            }


            Box(modifier = Modifier
                .fillMaxWidth(1f)
                .padding(start = 20.dp, top = 20.dp, end = 20.dp)) {
                Column(modifier = Modifier
                    .align(Alignment.CenterStart)
                    .fillMaxWidth(1f)) {
                    Text(
                        text = trainingDetailsViewModel.remainingTimeState.value,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Orange500,
                            fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                            textAlign = TextAlign.Start
                        )
                    )

                    Text(
                        text = trainingState.title,
                        style = customTextStyle
                    )
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Color.Black)) {
                                append("Presented by ")
                            }
                            append(trainingState.assignedTo.fullName)
                        },
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .clickable { showProfileDialog = true },
                        style = TextStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = Orange500,
                            fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                            textAlign = TextAlign.Start
                        )
                    )


                    val maxLines = if (isExpanded.value) Int.MAX_VALUE else 3
                    val showButton = remember(trainingState.description) {
                        mutableStateOf(false)
                    }


                    Text(
                        text = trainingState.description,
                        maxLines = maxLines,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
                        onTextLayout = { textLayoutResult ->
                            if ( textLayoutResult.hasVisualOverflow) {
                                showButton.value = true
                            }
                        },
                        style = TextStyle(
                            fontWeight = FontWeight.Light,
                            fontSize = 16.sp,
                            color = Color.Black,
                            fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                            textAlign = TextAlign.Start
                        ),
                        overflow = TextOverflow.Ellipsis,
                        softWrap = true
                    )


                    if(showButton.value == true){
                        Button(
                            onClick = { isExpanded.value = !isExpanded.value },
                            modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
                            colors = ButtonDefaults.textButtonColors(backgroundColor = Color.Black)
                        ) {
                            Text(
                                text = if (isExpanded.value) "Read Less" else "Read More",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                            )
                        }
                    }


                    Row(modifier = Modifier.fillMaxWidth(1f)){
                        Column(modifier = Modifier.fillMaxWidth(0.5f)){
                            Text(
                                text = "Date and time",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Grey2,
                                    fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                                    textAlign = TextAlign.Start
                                )
                            )
                            Text(
                                text = "From ${formated_startDate}",
                                style = TextStyle(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 18.sp,
                                    color = Color.Black,
                                    fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                                    textAlign = TextAlign.Start
                                )
                            )
                            Text(
                                text = "To ${formated_endDate}",
                                modifier = Modifier.padding(bottom = 16.dp),
                                style = TextStyle(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 18.sp,
                                    color = Color.Black,
                                    fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                                    textAlign = TextAlign.Start
                                )
                            )
                        }
                        Column(modifier = Modifier.fillMaxWidth(1f)){
                            Text(
                                text = "Duration",
                                modifier = Modifier.fillMaxWidth(1f),
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Grey2,
                                    fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                                    textAlign = TextAlign.Start
                                )
                            )
                            Text(
                                text = trainingState.duration,

                                modifier = Modifier
                                    .padding(bottom = 16.dp)
                                    .fillMaxWidth(1f),
                                style = TextStyle(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 18.sp,
                                    color = Color.Black,
                                    fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                                    textAlign = TextAlign.Start
                                )
                            )
                        }
                    }

                    if(!trainingState.onLine && !trainingState.isAsynchronous){
                        Text(
                            text = "Location",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Grey2,
                                fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                                textAlign = TextAlign.Start
                            )
                        )

                        Text(
                            text = trainingState.location,
                            modifier = Modifier.padding(bottom = 16.dp),
                            style = TextStyle(
                                fontWeight = FontWeight.Light,
                                fontSize = 18.sp,
                                color = Orange500,
                                fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                                textAlign = TextAlign.Start,
                                textDecoration = TextDecoration.Underline
                            )
                        )

                    } else if (trainingState.onLine && trainingState.isAsynchronous){
                        Text(
                            text = "Link",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Grey2,
                                fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                                textAlign = TextAlign.Start
                            )
                        )

                        LinkText(
                            text = "visit the course URL",
                            link = trainingState.link, // Replace with your desired link
                            modifier = Modifier.padding(bottom = 16.dp),
                            style = TextStyle(
                                fontWeight = FontWeight.Light,
                                fontSize = 18.sp,
                                color = Orange500,
                                fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                                textAlign = TextAlign.Start,
                                textDecoration = TextDecoration.Underline
                            )
                        )
                    }
                    Text(
                        text = "Planning of the ${trainingState.planing.size} days",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Grey2,
                            fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                            textAlign = TextAlign.Start
                        )
                    )

                    PlanningSection(trainingState.planing)

                    Text(
                        text = "Goals",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Grey2,
                            fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                            textAlign = TextAlign.Start
                        )
                    )

                    GoalsSection(trainingState.goals)


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {

                        val isLoading by trainingDetailsViewModel.isLoading.collectAsState()


                        Button(
                            colors = ButtonDefaults.textButtonColors(backgroundColor = Orange500),
                            onClick = { trainingDetailsViewModel.participate() },
                            modifier = Modifier
                                .wrapContentWidth()
                                .padding(top = 10.dp, bottom = 10.dp, end = 10.dp)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .padding(end = 8.dp)
                                )
                            }

                            Text(
                                text = trainingDetailsViewModel.button_text_paticipation.value,
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }



                            if(hasVoted == false && trainingDetailsViewModel.isAccepted.value && trainingState.activateQuizEvaluation == true && trainingState.isQuizzEvaluation == true && trainingState.quizzEvaluation != null){
                                Button(modifier = Modifier
                                    .wrapContentWidth()
                                    .padding(top = 10.dp, bottom = 10.dp, start = 10.dp),

                                    onClick = { navController.navigate("details/quizz/${trainingState.quizzEvaluation}") }) {
                                    Text(text = "Pass the test")

                                }
                            }
                    }


                }
            }

        }
    }

}

@Composable
fun RoundedHashButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val buttonShape = RoundedCornerShape(
        topStart = CornerSize(percent = 50),
        topEnd = CornerSize(percent = 50),
        bottomStart = CornerSize(percent = 50),
        bottomEnd = CornerSize(percent = 50),
    )
    Button(
        onClick = onClick,
        shape = buttonShape,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.White,
            contentColor = Color.Black
        ),
        border = BorderStroke(2.dp, Grey),
    ) {
        Text(text = text, style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.neue_helvetica)),
            textAlign = TextAlign.Start
        ))
    }
}

@Composable
fun ModalityRoundedButton(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Black,
) {
    val buttonShape = RoundedCornerShape(percent = 50)
    Button(
        onClick = { /* No action here */ },
        shape = buttonShape,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = Color.White
        ),
        modifier = modifier
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(Color.Green, shape = CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                    textAlign = TextAlign.Start
                )
            )
        }
    }
}

@Composable
fun IsQuizz(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Black,
) {
    val buttonShape = RoundedCornerShape(percent = 50)
    Button(
        onClick = { /* No action here */ },
        shape = buttonShape,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = Color.White
        ),
        modifier = modifier
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.Green,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                    textAlign = TextAlign.Start
                )
            )
        }
    }
}




@Composable
fun ImageFromURL(url: String) {
    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current).data(data = url).apply(block = fun ImageRequest.Builder.() {
            crossfade(true)
        }).build()
    )

    val imageModifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1f)
    Box(modifier = imageModifier) {
        Image(
            painter = painter,
            contentDescription = "Training Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        when (painter.state) {
            is AsyncImagePainter.State.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))


            }
            is AsyncImagePainter.State.Success -> {
                Image(
                    painter = painter,
                    contentDescription = "Training Image",
                    modifier = Modifier
                        .fillMaxHeight(1f)
                        .fillMaxWidth(1f),
                    contentScale = ContentScale.Crop
                )
            }
            else -> {
                Image(
                    painter = painterResource(R.drawable.detail_cover),
                    contentDescription = "Default Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}




@Composable
fun DailyContent(day: Planing) {
    Column(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,

            ) {
            Canvas(modifier = Modifier.size(8.dp)) {
                drawCircle(color = Color.Black)

            }
            Text(
                text = "Day ${day.daysNumber + 1} at ${day.startingHour} - ${day.endingHour}",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black,
                    textDecoration = TextDecoration.Underline
                ),
                modifier = Modifier.padding(start= 14.dp,bottom = 4.dp, top = 4.dp)
            )
        }

        day.dailyContent.forEach { content ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
            ) {


                Spacer(modifier = Modifier.width(8.dp))

                Canvas(modifier = Modifier.size(8.dp)) {
                    drawCircle(color = Orange500)
                }

                Text(
                    text = content,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Black
                    ),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
fun PlanningSection(planing: List<Planing>) {
    Column(modifier = Modifier.padding(start = 20.dp)) {

        planing.forEach { day ->
            DailyContent(day)
        }
    }
}

@Composable
fun GoalsSection(goals: List<String>) {
    Column(modifier = Modifier.padding(start = 20.dp)) {

        goals.forEach { goal ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,

                    ) {
                    Canvas(modifier = Modifier.size(8.dp)) {
                        drawCircle(color = Color.Black)

                    }
                    Text(
                        text = goal,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black,
                            textDecoration = TextDecoration.Underline
                        ),
                        modifier = Modifier.padding(start = 14.dp, bottom = 4.dp, top = 4.dp)
                    )
                }
            }

        }
    }
}



@Composable
fun DialogWithCloseIcon(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    title: String,
    content: @Composable () -> Unit
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .fillMaxHeight(0.73f)

                    .background(Color.Transparent)
            ) {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 10.dp, end = 10.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(color = Orange500)) {
                                    append(title)
                                }
                                withStyle(style = SpanStyle(color = Color.Black, fontSize = 22.sp)) {
                                    append("'s profile")
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 10.dp),
                            textAlign = TextAlign.Start,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                fontFamily = FontFamily(Font(R.font.neue_helvetica))
                            ),
                            softWrap = true,
                            overflow = TextOverflow.Visible
                        )
                        IconButton(
                            onClick = onDismiss
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.Gray
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    content()
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TrainerProfile(trainer: TrainerProfileDomainModel) {

        Column(modifier = Modifier
            .fillMaxWidth(1f)
            .fillMaxHeight(1f)) {

            ProfileItemEmail(
                icon = Icons.Default.Email,
                text = trainer.email
            )
            ProfileItem(
                icon = Icons.Default.Phone,
                text = trainer.phoneNumber
            )
            ProfileItem(
                icon = Icons.Default.LocationOn,
                text = trainer.direction
            )
            ProfileItem(
                icon = Icons.Default.Face,
                text = trainer.gender
            )
            ProfileItem(
                icon = Icons.Default.Business,
                text = trainer.departement
            )
            ProfileItem(
                icon = Icons.Default.Work,
                text =  trainer.role
            )
            FlowRow(modifier = Modifier
                .fillMaxWidth(1f)
                .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)) {
                trainer.skills.forEach { skill ->
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .background(Color.LightGray, RoundedCornerShape(4.dp))
                            .padding(8.dp)
                    ) {
                        Text(skill)
                    }
                }
        }

    }

}

@Composable
fun ProfileItem(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Icon",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                textAlign = TextAlign.Start
            )
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileItemEmail(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Icon",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier.basicMarquee(),
            text = text,
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                textAlign = TextAlign.Start
            )
        )
    }
}
@Composable
fun LinkText(
    text: String,
    link: String,
    modifier: Modifier,
    style: TextStyle,
) {
    val annotatedText = buildAnnotatedString {
        withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) {
            append(text)
            addStringAnnotation("URL", link, start = 0, end = text.length)
        }
    }
    val ctx = LocalContext.current
    ClickableText(
        modifier = modifier,
        style = style,
        text = annotatedText,
        onClick = {
            val urlIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(link)
            )
            ctx.startActivity(urlIntent)
        }
    )
}

private fun calculateLineCount(text: String): Int {
    // Split the text into lines based on the newline character ("\n")
    val lines = text.split("\n")
    return lines.size
}

@Composable
fun ExpandableText(
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    text: String,
    collapsedMaxLine: Int = 3,
    showMoreText: String = "... Show More",
    showMoreStyle: SpanStyle = SpanStyle(fontWeight = FontWeight.W500),
    showLessText: String = " Show Less",
    showLessStyle: SpanStyle = showMoreStyle,
    textAlign: TextAlign? = null
) {
    var isExpanded by remember { mutableStateOf(false) }
    var clickable by remember { mutableStateOf(false) }
    var lastCharIndex by remember { mutableStateOf(0) }
    Box(modifier = Modifier
        .clickable(clickable) {
            isExpanded = !isExpanded
        }
        .then(modifier)
    ) {
        Text(
            modifier = textModifier
                .fillMaxWidth()
                .animateContentSize(),
            text = buildAnnotatedString {
                if (clickable) {
                    if (isExpanded) {
                        append(text)
                        withStyle(style = showLessStyle) { append(showLessText) }
                    } else {
                        val adjustText = text.substring(startIndex = 0, endIndex = lastCharIndex)
                            .dropLast(showMoreText.length)
                            .dropLastWhile { Character.isWhitespace(it) || it == '.' }
                        append(adjustText)
                        withStyle(style = showMoreStyle) { append(showMoreText) }
                    }
                } else {
                    append(text)
                }
            },
            maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLine,
            onTextLayout = { textLayoutResult ->
                if (!isExpanded && textLayoutResult.hasVisualOverflow) {
                    clickable = true
                    lastCharIndex = textLayoutResult.getLineEnd(collapsedMaxLine - 1)
                }
            },
            style = style,
            textAlign = textAlign
        )
    }

}