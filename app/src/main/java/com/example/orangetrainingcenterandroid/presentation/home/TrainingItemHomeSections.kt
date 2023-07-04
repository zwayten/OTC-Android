package com.example.orangetrainingcenterandroid.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.orangetrainingcenterandroid.R
import com.example.orangetrainingcenterandroid.common.BASE_URL2
import com.example.orangetrainingcenterandroid.domain.training.model.TrainingDomainModel
import com.example.orangetrainingcenterandroid.ui.theme.Grey
import com.example.orangetrainingcenterandroid.ui.theme.Grey2
import com.example.orangetrainingcenterandroid.ui.theme.Orange500

@Composable
fun TrainingItem2(
    training: TrainingDomainModel,
    homeViewModel: HomeViewModel,
    onItemClick: (String) -> Unit
) {
    val customTextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        fontFamily = FontFamily(Font(R.font.neue_helvetica)),
        textAlign = TextAlign.Start
    )
    val formated_startDate = homeViewModel.changeDateFormat(training.startDate, "MMM dd yyyy")
    val formated_endDate = homeViewModel.changeDateFormat(training.endDate, "MMM dd yyyy")
    Card(
        modifier = Modifier
            .width(350.dp)
            .fillMaxHeight(1f)
            .padding()
            .clickable { onItemClick(training._id) },
        elevation = 8.dp,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(10.dp),
        contentColor = Orange500
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(1f)
        ) {
            //vertical first half
            Row(modifier = Modifier.fillMaxWidth(1f).fillMaxHeight(0.5f)){
                Box(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .fillMaxHeight(1f)

                ) {
                    ImageFromURL("$BASE_URL2${training.cover}")
                }
            }
            //vertical second half
            Row(modifier = Modifier.fillMaxWidth(1f).fillMaxHeight(0.65f).padding(start = 10.dp, end= 10.dp, top= 10.dp))
            {
                //left elements
                Column( modifier = Modifier
                    .fillMaxWidth(0.5f).fillMaxHeight(1f)){
                    //Title
                    Text(
                        text = training.title,
                        style = customTextStyle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    //start Date
                    Text(
                        text = "From $formated_startDate",
                        style = TextStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Color.Black,
                            fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                            textAlign = TextAlign.Start
                        )
                    )

                    //End Date
                    Text(
                        text = "To $formated_endDate",
                        modifier = Modifier.padding(bottom = 16.dp),
                        style = TextStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Color.Black,
                            fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                            textAlign = TextAlign.Start
                        )
                    )
                }


                //right elements
                Column( modifier = Modifier
                    .fillMaxWidth(1f).fillMaxHeight(1f)){
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
                        text = training.duration,

                        modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth(1f),
                        style = TextStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Color.Black,
                            fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                            textAlign = TextAlign.Start
                        )
                    )
                    Text(
                        text = "Presented by ${training.assignedTo}",
                        modifier = Modifier.padding(bottom = 8.dp),
                        style = TextStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = Color.Black,
                            fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                            textAlign = TextAlign.Start
                        )
                    )
                }
            }


            //themes an chips
            Row(
                modifier = Modifier.padding(start = 10.dp, end= 10.dp, top= 10.dp, bottom = 10.dp).fillMaxWidth(1f).fillMaxHeight(1f)
            ) {

                RoundedHashButton(
                    text = "# ${training.theme}",
                    onClick = { /* Handle button click */ },
                    modifier = Modifier.weight(1f)
                )


                if (training.onLine) {
                    ModalityRoundedButton(
                        text = "Online",
                        backgroundColor = Color.Black,
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(start = 5.dp)
                    )
                } else {
                    ModalityRoundedButton(
                        text = "Face-To-Face",
                        backgroundColor = Color.Black,
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(start = 5.dp)
                    )
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
            textAlign = TextAlign.Start,
            fontSize = 14.sp,
        )
        )
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
                    textAlign = TextAlign.Start,
                    fontSize = 14.sp,
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
        .fillMaxHeight(1f).fillMaxWidth(1f)
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
                    modifier = Modifier.fillMaxHeight(1f).fillMaxWidth(1f),
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