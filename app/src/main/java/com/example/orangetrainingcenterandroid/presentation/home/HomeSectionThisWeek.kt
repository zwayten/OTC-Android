package com.example.orangetrainingcenterandroid.presentation.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

import java.time.LocalDate


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeSectionThisWeek(
    homeViewModel: HomeViewModel,
    navController: NavController,
    onTrainingItemClick: (String) -> Unit
) {

    val lazyListState = rememberLazyListState()
    val trainingsState by homeViewModel.trainingsStateThisWeek.collectAsState()
    val isTrainingsEmpty = trainingsState.isEmpty()

    LazyRow(
        modifier = Modifier
            //.fillMaxHeight(1f)
            .height(400.dp)
            //.wrapContentHeight()
            .padding(start = 10.dp, end = 10.dp)
            .background(MaterialTheme.colors.background),
        contentPadding = PaddingValues(horizontal = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        state = lazyListState,
    )
    {
        if (isTrainingsEmpty) {
            item {
                Text("No data available")
            }
        } else {
            items(trainingsState) { training ->
                TrainingItem2(training = training, homeViewModel, onItemClick = onTrainingItemClick)
            }
        }


    }
}


@Composable
fun HomeSectionTargeted(
    homeViewModel: HomeViewModel,
    navController: NavController,
    onTrainingItemClick: (String) -> Unit
) {

    val lazyListState = rememberLazyListState()
    val trainingsState by homeViewModel.trainingsStateTargeted.collectAsState()
    val isTrainingsEmpty = trainingsState.isEmpty()

    LazyRow(
        modifier = Modifier
            //.fillMaxHeight(1f)
            .height(400.dp)
            //.wrapContentHeight()
            .padding(start = 10.dp, end = 10.dp)
            .background(MaterialTheme.colors.background),
        contentPadding = PaddingValues(horizontal = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        state = lazyListState,
    )
    {
        if (isTrainingsEmpty) {
            item {
                Text("No data available")
            }
        } else {
            items(trainingsState) { training ->
                TrainingItem2(training = training, homeViewModel, onItemClick = onTrainingItemClick)
            }
        }

    }
}

@Composable
fun HomeSectionUpComing(
    homeViewModel: HomeViewModel,
    navController: NavController,
    onTrainingItemClick: (String) -> Unit
) {

    val lazyListState = rememberLazyListState()
    val trainingsState by homeViewModel.trainingsStateUpComing.collectAsState()
    val isTrainingsEmpty = trainingsState.isEmpty()

    LazyRow(
        modifier = Modifier
            //.fillMaxHeight(1f)
            .height(400.dp)
            //.wrapContentHeight()
            .padding(start = 10.dp, end = 10.dp)
            .background(MaterialTheme.colors.background),
        contentPadding = PaddingValues(horizontal = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        state = lazyListState,
    )
    {
        if (isTrainingsEmpty) {
            item {
                Text("No data available")
            }
        } else {
            items(trainingsState) { training ->
                TrainingItem2(training = training, homeViewModel, onItemClick = onTrainingItemClick)
            }
        }


    }
}

