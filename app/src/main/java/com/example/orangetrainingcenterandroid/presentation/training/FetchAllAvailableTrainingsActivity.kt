package com.example.orangetrainingcenterandroid.presentation.training


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.orangetrainingcenterandroid.R
import com.example.orangetrainingcenterandroid.common.BASE_URL2
import com.example.orangetrainingcenterandroid.domain.training.model.TrainingDomainModel
import com.example.orangetrainingcenterandroid.ui.theme.Grey
import com.example.orangetrainingcenterandroid.ui.theme.Orange500
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TrainingScreen(
    fetchAllAvailableTrainingsViewModel: FetchAllAvailableTrainingsViewModel,
    navController: NavController,
    startDateParam: String?,
    fromHome:Boolean,
    endDateParam: String?,
    onTrainingItemClick: (String) -> Unit
) {
    println("trainings screen")
    val lazyListState = rememberLazyListState()
    val searchQuery = remember { mutableStateOf("") }
    val selectedTheme = remember { mutableStateOf("") }
    val selectedStartDate = remember { mutableStateOf<LocalDate?>(null) }
    val selectedEndDate = remember { mutableStateOf<LocalDate?>(null) }
    val trainingsState by fetchAllAvailableTrainingsViewModel.trainingsState.collectAsState()
    val loading by fetchAllAvailableTrainingsViewModel.loading.collectAsState()
    val lastVisibleItem = remember { mutableStateOf(0) }
    val loadMoreThreshold = 0
    val currentCursor by fetchAllAvailableTrainingsViewModel.currentCursor.collectAsState()



    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current



    LaunchedEffect(navBackStackEntry) {
        navBackStackEntry?.let { entry ->
            if (endDateParam != null && startDateParam != null && fromHome == true) {


                val startDateFromNav = navController.currentBackStackEntry?.arguments?.getString("startDateParam")
                val endDateFromNav =navController.currentBackStackEntry?.arguments?.getString("endDateParam")



                selectedStartDate.value = startDateFromNav?.let { LocalDate.parse(it) }
                selectedEndDate.value = endDateFromNav?.let { LocalDate.parse(it) }

            }
            fetchAllAvailableTrainingsViewModel.loadThemes()
            fetchAllAvailableTrainingsViewModel.fetchData(
                searchQuery.value,
                selectedTheme.value,
                selectedStartDate.value,
                selectedEndDate.value,
                ""
            )
        }
    }

    LaunchedEffect( selectedStartDate.value, selectedEndDate.value) {

        fetchAllAvailableTrainingsViewModel.clearTrainingsState()
        fetchAllAvailableTrainingsViewModel.clearCurrentCursor()
        fetchAllAvailableTrainingsViewModel.fetchData(
            searchQuery.value,
            selectedTheme.value,
            selectedStartDate.value,
            selectedEndDate.value,
            ""
        )
    }

    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                val lastVisibleIndex = visibleItems.lastOrNull()?.index ?: 0
                lastVisibleItem.value = lastVisibleIndex
            }
    }

    val isTrainingsEmpty = trainingsState.isEmpty()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp, end = 10.dp)
            .background(MaterialTheme.colors.background),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        state = lazyListState
    ) {
        stickyHeader {
            Column(modifier = Modifier.wrapContentSize().background(Color.White)){
                SearchBar(
                    searchQuery = searchQuery.value,
                    onSearchQueryChange = { query ->
                        searchQuery.value = query
                        fetchAllAvailableTrainingsViewModel.fetchData(
                            query,
                            selectedTheme.value,
                            selectedStartDate.value,
                            selectedEndDate.value,
                            ""
                        )
                    }
                )

                TrainingFilterSection(
                    themes = fetchAllAvailableTrainingsViewModel.themeNamesState,
                    selectedTheme = selectedTheme.value,
                    onThemeSelected = { theme ->
                        selectedTheme.value = if (selectedTheme.value == theme) "" else theme
                        fetchAllAvailableTrainingsViewModel.fetchData(
                            searchQuery.value,
                            selectedTheme.value,
                            selectedStartDate.value,
                            selectedEndDate.value,
                            ""
                        )
                    },
                    onStartDateSelected = { date -> selectedStartDate.value = date },
                    onEndDateSelected = { date -> selectedEndDate.value = date },
                    initialStartDate = selectedStartDate.value,
                    initialEndDate = selectedEndDate.value
                )
            }

        }

        if (isTrainingsEmpty) {
            item {
                Text("No data available")
            }
        } else {
            items(trainingsState) { training ->
                TrainingItem2(training = training, fetchAllAvailableTrainingsViewModel, onItemClick = onTrainingItemClick)
            }
        }

        if (lastVisibleItem.value >= trainingsState.size - loadMoreThreshold &&
            fetchAllAvailableTrainingsViewModel.hasMoreData &&
            !loading
        ) {
            fetchAllAvailableTrainingsViewModel.fetchData(
                searchQuery.value,
                selectedTheme.value,
                selectedStartDate.value,
                selectedEndDate.value,
                currentCursor
            )
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
fun TrainingItem2(
    training: TrainingDomainModel,
    fetchAllAvailableTrainingsViewModel: FetchAllAvailableTrainingsViewModel,
    onItemClick: (String) -> Unit
) {





    val customTextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        fontFamily = FontFamily(Font(R.font.neue_helvetica)),
        textAlign = TextAlign.Start
    )
    val formated_startDate = fetchAllAvailableTrainingsViewModel.changeDateFormat(training.startDate, "MMM dd yyyy")
    val formated_endDate = fetchAllAvailableTrainingsViewModel.changeDateFormat(training.endDate, "MMM dd yyyy")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(225.dp)
            .padding()
            .clickable { onItemClick(training._id) },
        elevation = 8.dp,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(10.dp),
        contentColor = Orange500
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier

                    .fillMaxWidth(0.5f)
                    .fillMaxHeight(1f)
                     // Adjust the size as needed
            ) {
                ImageFromURL("$BASE_URL2${training.cover}")


            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                //On Going, if not display the days remaining
               /* Text(
                    text = fetchAllAvailableTrainingsViewModel.calculateRemainingTime(training.startDate),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Orange500,
                        fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                        textAlign = TextAlign.Start
                    )
                )*/
                Text(
                    text = training.title,
                    style = customTextStyle,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))
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
                /*Text(
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
                */
                Text(
                    text = " ${training.duration}",
                    modifier = Modifier.padding(bottom = 16.dp),
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
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                LazyRow(
                    modifier = Modifier.padding(top = 5.dp)
                ) {
                    item {
                        RoundedHashButton(
                            text = "# ${training.theme}",
                            onClick = { /* Handle button click */ },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    item {
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
        .fillMaxHeight(1f)
        .fillMaxWidth(1f)
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



