package com.example.orangetrainingcenterandroid.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.util.Size
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Help
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.currentBackStackEntryAsState
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.orangetrainingcenterandroid.R
import com.example.orangetrainingcenterandroid.common.BASE_URL2
import com.example.orangetrainingcenterandroid.domain.training.model.PresenceStateDomainModel
import com.example.orangetrainingcenterandroid.presentation.home.*
import com.example.orangetrainingcenterandroid.presentation.poll.PollFrame
import com.example.orangetrainingcenterandroid.presentation.training.ImageFromURL
import com.example.orangetrainingcenterandroid.ui.theme.Orange500
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.time.LocalDate
import androidx.camera.core.Preview.Builder as PreviewBuilder
import androidx.camera.core.ImageAnalysis.Builder as ImageAnalysisBuilder

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    navController: NavController,
    onTrainingItemClick: (String) -> Unit
) {



    println("Home screen")

    val scrollState = rememberScrollState()

    val selectedTrainingId = remember { mutableStateOf("") }
    //params for this Week Section
    val startDateParam = LocalDate.now().toString()
    val endDateParam = LocalDate.now().plusDays(7).toString()

    val compositionLottie by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.quizzanimation))

    homeViewModel.loadHomeScreen()

    val scannerEnabled = remember { mutableStateOf(false) }
    val scannedCode = remember { mutableStateOf("") }

    val showToast = remember { mutableStateOf(false) }
    val toastMessage = remember { mutableStateOf("") }

    val customTextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        fontFamily = FontFamily(Font(R.font.neue_helvetica)),
        textAlign = TextAlign.Start
    )

    Box(
        modifier = Modifier
            .fillMaxSize()

            .padding(bottom = 20.dp)
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(bottom = 20.dp)
        ) {

            //Poll
            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(10.dp)
            ) {
                homeViewModel.poll.value?.let { PollFrame(it, homeViewModel) }
            }


            //Animation Quizz:

            if (homeViewModel.animationQuizz.value != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(top = 20.dp, start = 10.dp)
                )
                {
                    Text(
                        text = "Animation Quiz",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                            textAlign = TextAlign.Start
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(10.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .wrapContentHeight(),
                        elevation = 8.dp,
                        backgroundColor = Color.White,
                        shape = RoundedCornerShape(10.dp),
                    ) {
                        Row(modifier = Modifier.fillMaxSize()) {
                            LottieAnimation(
                                modifier = Modifier
                                    .fillMaxWidth(0.5f)
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(
                                        vertical = 0.dp,
                                        horizontal = 0.dp
                                    ), // Adjust vertical padding as needed
                                composition = compositionLottie
                            )


                            // Other elements on the right side
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(10.dp)
                            ) {
                                val customTextStyle = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                                    textAlign = TextAlign.Start
                                )
                                Text(
                                    text = homeViewModel.animationQuizz.value!!.description,
                                    style = customTextStyle,
                                    maxLines = 4,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Button(onClick = { navController.navigate("details/quizz/${homeViewModel.animationQuizz.value!!._id}") }) {
                                    Text("Pass quiz")
                                }
                            }
                        }
                    }
                }
            }


            //current training
            if (homeViewModel.currentTraining.value != null) {
                println("current training")
                Row(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(top = 20.dp, start = 10.dp)
                )
                {
                    Text(
                        text = "Training On Going",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                            textAlign = TextAlign.Start
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                    )
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(10.dp)
                        .clickable { navController.navigate("details/${homeViewModel.currentTraining.value!!._id}") },
                    //.clickable { onItemClick(training._id) },
                    elevation = 8.dp,
                    backgroundColor = Color.White,
                    shape = RoundedCornerShape(10.dp),
                    contentColor = Orange500
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {
                            Text(
                                modifier = Modifier.basicMarquee(),
                                text = homeViewModel.currentTraining.value!!.title,
                                style = customTextStyle,
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {
                            val formated_startDate = homeViewModel.changeDateFormat(
                                homeViewModel.currentTraining.value!!.startDate,
                                "MMM dd yyyy"
                            )
                            val formated_endDate = homeViewModel.changeDateFormat(
                                homeViewModel.currentTraining.value!!.endDate,
                                "MMM dd yyyy"
                            )
                            Text(
                                text = "From ",
                                modifier = Modifier.padding(bottom = 16.dp),
                                style = TextStyle(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = Orange500,
                                    fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                                    textAlign = TextAlign.Start
                                )
                            )
                            Text(
                                text = formated_startDate,
                                style = TextStyle(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = Color.Black,
                                    fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                                    textAlign = TextAlign.Start
                                )
                            )
                            Text(
                                text = " To ",
                                modifier = Modifier.padding(bottom = 16.dp),
                                style = TextStyle(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = Orange500,
                                    fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                                    textAlign = TextAlign.Start
                                )
                            )
                            Text(
                                text = formated_endDate,
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

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {
                            Text(
                                text = "Remaining time: ",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color.Black,
                                    fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                                    textAlign = TextAlign.Start
                                )
                            )
                            Text(
                                text = homeViewModel.calculateRemainingTime(homeViewModel.currentTraining.value!!.endDate),
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Orange500,
                                    fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                                    textAlign = TextAlign.Start
                                )
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {
                            //TODO planning days attended
                            homeViewModel.presenceState.value?.let { PlanningRow(it) }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                        ) {
                            homeViewModel.currentTraining.value!!.isQuizzEvaluation?.let {
                                val hasVoted by homeViewModel.hasVoted.collectAsState()
                                if(hasVoted == false &&  homeViewModel.currentTraining.value!!.activateQuizEvaluation == true &&homeViewModel.currentTraining.value!!.isQuizzEvaluation == true && homeViewModel.currentTraining.value!!.quizzEvaluation != null)
                                {
                                    Button(modifier = Modifier
                                        .wrapContentWidth(),

                                        enabled = true,
                                        onClick = { navController.navigate("details/quizz/${homeViewModel.currentTraining.value!!.quizzEvaluation}") }) {
                                        Text(text = "Take the test")

                                    }
                                }
                            }
                            Button(modifier = Modifier
                                .wrapContentWidth().padding(start = 5.dp),

                                enabled = true,
                                onClick = { scannerEnabled.value = !scannerEnabled.value }) {
                                Text(text = "Attend")

                            }
                            /* Text(
                                 text = scannedCode.value,
                                 fontSize = 18.sp,
                                 fontWeight = FontWeight.Bold,
                                 textAlign = TextAlign.Center,
                                 modifier = Modifier
                                     .fillMaxWidth()
                                     .padding(16.dp)
                             )*/
                        }
                       /* if (scannerEnabled.value) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .wrapContentHeight()
                                    .padding(top = 20.dp, start = 10.dp)
                            ) {
                                if (scannerEnabled.value) {
                                    QrCodeScanner(onQrCodeScanned = { code ->
                                        scannedCode.value = code
                                        scannerEnabled.value = false
                                        val json = JSONObject(scannedCode.value)
                                        val trainingId = json.getString("trainingId")
                                        if(trainingId != homeViewModel.currentTraining.value!!._id){
                                            showToast.value = true
                                            toastMessage.value = "Invalid Training"
                                        }

                                        else {
                                            homeViewModel.confirmPresence(trainingId)
                                            homeViewModel.getPreseneceState(trainingId)
                                        }
                                    })
                                }
                            }
                        }
                        */


                    }

                }
            }

//this week

            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(top = 20.dp, end = 10.dp, start = 10.dp)
            ) {
                TextWithArrow("This Week", onClick = {
                    val fromHome = true
                    if (fromHome) {
                        navController.navigate("trainings?startDate=${startDateParam}&endDate=${endDateParam}&fromHome=$fromHome")
                    } else {

                        navController.navigate("trainings?fromHome=${!fromHome}")
                    }

                })
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
//.height(350.dp)
                    .wrapContentHeight()
                    .padding(0.dp)
            ) {
                HomeSectionThisWeek(
                    homeViewModel,
                    navController,
                    onTrainingItemClick = { trainingId ->
                        selectedTrainingId.value = trainingId
                        onTrainingItemClick(trainingId)
                    })
            }
            //upcoming
            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(top = 20.dp, end = 10.dp, start = 10.dp)
            ) {
                TextWithArrow("Upcoming", onClick = {

                        navController.navigate("trainings")


                })
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
//.height(350.dp)
                    .wrapContentHeight()
                    .padding(0.dp)
            ) {
                HomeSectionUpComing(
                    homeViewModel,
                    navController,
                    onTrainingItemClick = { trainingId ->
                        selectedTrainingId.value = trainingId
                        onTrainingItemClick(trainingId)
                    })
            }

        }

    }
    if (scannerEnabled.value) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            QrCodeScanner(onQrCodeScanned = { code ->
                scannedCode.value = code
                scannerEnabled.value = false
                val json = JSONObject(scannedCode.value)
                val trainingId = json.getString("trainingId")
                if (trainingId != homeViewModel.currentTraining.value!!._id) {
                    showToast.value = true
                    toastMessage.value = "Invalid Training"
                } else {
                    homeViewModel.confirmPresence(trainingId)
                    homeViewModel.getPreseneceState(trainingId)
                }
            })

            IconButton(
                onClick = {
                    scannerEnabled.value = false
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White
                )
            }
        }
    }



}

@Composable
fun TextWithArrow(textContent: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),

        ) {
        Text(
            text = textContent,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                textAlign = TextAlign.Start
            ),
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )

        Text(
            text = "View All",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                textAlign = TextAlign.Start
            ),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .clickable { onClick.invoke() }
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "Arrow",
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.CenterVertically)
        )
    }
}

/*
@Composable
fun QrCodeScanner(
    onQrCodeScanned: (String) -> Unit
) {
    var code by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }
    var hasCamPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCamPermission = granted
        }
    )
    LaunchedEffect(key1 = true) {
        launcher.launch(Manifest.permission.CAMERA)
    }

    if (hasCamPermission) {
        val coroutineScope = rememberCoroutineScope()
        AndroidView(
            factory = { context ->
                val previewView = PreviewView(context)
                val preview = PreviewBuilder().build()
                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()
                preview.setSurfaceProvider(previewView.surfaceProvider)
                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                imageAnalysis.setAnalyzer(
                    ContextCompat.getMainExecutor(context),
                    QrCodeAnalyzer { result ->
                        code = result
                        onQrCodeScanned(result) // Pass the scanned QR code to the calling code
                    }
                )
                try {
                    cameraProviderFuture.get().bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                previewView
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
*/



@Composable
fun QrCodeScanner(
    onQrCodeScanned: (String) -> Unit
) {
    var code by remember {
        mutableStateOf("")
    }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }
    var hasCamPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCamPermission = granted
        }
    )
    LaunchedEffect(key1 = true) {
        launcher.launch(Manifest.permission.CAMERA)
    }

    if (hasCamPermission) {
        val coroutineScope = rememberCoroutineScope()
        AndroidView(
            factory = { context ->
                val previewView = PreviewView(context)
                val cameraProvider = cameraProviderFuture.get()
                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()
                val preview = PreviewBuilder()
                    .setTargetResolution(Size(1280, 720))
                    .build()
                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .setTargetResolution(Size(1280, 720))
                    .build()
                imageAnalysis.setAnalyzer(
                    ContextCompat.getMainExecutor(context),
                    QrCodeAnalyzer { result ->
                        code = result
                        onQrCodeScanned(result) // Pass the scanned QR code to the calling code
                    }
                )
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )
                    preview.setSurfaceProvider(previewView.surfaceProvider)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                previewView
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 5.dp, start = 10.dp, end = 10.dp)
        )
    }
}
@Composable
fun PlanningRow(presenceState: PresenceStateDomainModel) {
    val currentDayIndex = presenceState.presenceState.indexOfFirst { it.isCurrentDay }

    Row(verticalAlignment = Alignment.CenterVertically) {
        for ((index, day) in presenceState.presenceState.withIndex()) {
            val dotColor = if (index < currentDayIndex) {
                if (day.attended) Color.Red else Color.Green
            } else {
                Color.Gray
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                when {
                    day.attended -> {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.Green,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                    index > currentDayIndex -> {
                        Icon(
                            imageVector = Icons.Outlined.Help,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                    else -> {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
                Text(
                    text = day.dayNumber.toString(),
                    modifier = Modifier.padding(top = 4.dp),
                    style = TextStyle(fontSize = 12.sp, color = Color.Black)
                )
            }
        }
    }
}
@Composable
fun DisplayToastMessage(toastMessage: String) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(toastMessage) {
        coroutineScope.launch {
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
        }
    }
}
