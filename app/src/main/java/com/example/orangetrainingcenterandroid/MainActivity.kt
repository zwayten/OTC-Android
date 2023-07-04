package com.example.orangetrainingcenterandroid


import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.orangetrainingcenterandroid.presentation.HomeScreen
import com.example.orangetrainingcenterandroid.presentation.home.HomeViewModel
import com.example.orangetrainingcenterandroid.presentation.login.LoginActivity
import com.example.orangetrainingcenterandroid.presentation.menu.*
import com.example.orangetrainingcenterandroid.presentation.profile.ProfileScreen
import com.example.orangetrainingcenterandroid.presentation.profile.ProfileViewModel
import com.example.orangetrainingcenterandroid.presentation.quizzevaluation.QuizzEvaluationViewModel
import com.example.orangetrainingcenterandroid.presentation.quizzevaluation.QuizzScreen
import com.example.orangetrainingcenterandroid.presentation.settings.SettingsTab
import com.example.orangetrainingcenterandroid.presentation.settings.SettingsViewModel
import com.example.orangetrainingcenterandroid.presentation.training.FetchAllAvailableTrainingsViewModel
import com.example.orangetrainingcenterandroid.presentation.training.TrainingScreen
import com.example.orangetrainingcenterandroid.presentation.training_details.TrainingDetailsScreen
import com.example.orangetrainingcenterandroid.presentation.training_details.TrainingDetailsViewModel
import com.example.orangetrainingcenterandroid.ui.theme.Orange500
import com.example.orangetrainingcenterandroid.ui.theme.OrangeTrainingCenterAndroidTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var profileViewModel: ProfileViewModel

    @Inject
    lateinit var fetchAllAvailableTrainingsViewModel: FetchAllAvailableTrainingsViewModel

    @Inject
    lateinit var trainingDetailsViewModel: TrainingDetailsViewModel

    @Inject
    lateinit var homeViewModel: HomeViewModel
/*
    @Inject
    lateinit var settingsViewModel: SettingsViewModel

 */

    @Inject
    lateinit var quizzEvaluationViewModel: QuizzEvaluationViewModel


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.setLoggedIn2()
        val isLoggedIn = mainViewModel.isLoggedIn.value


        if (!isLoggedIn) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
            return
        }

        mainViewModel.checkTokenValidity()
        setContent {
            val bottomBarItems = listOf(
                BottomNavItem(
                    title = "home",
                    icon = Icons.Default.Home,
                    selectedContentColor = Orange500,
                    unselectedContentColor = Color.Black
                ),
                BottomNavItem(
                    title = "trainings",
                    icon = Icons.Default.Newspaper,
                    selectedContentColor = Orange500,
                    unselectedContentColor = Color.Black
                ),
                BottomNavItem(
                    title = "profile",
                    icon = Icons.Default.Person,
                    selectedContentColor = Orange500,
                    unselectedContentColor = Color.Black
                ),
                /*
                BottomNavItem(
                    title = "settings",
                    icon = Icons.Default.Settings,
                    selectedContentColor = Orange500,
                    unselectedContentColor = Color.Black
                )*/
            )
            val selectedIndex by mainViewModel.selectedTabIndex.collectAsState()
            val navController = rememberNavController()
           // val loggedIn = mainViewModel.isLoggedIn.observeAsState(false)

            OrangeTrainingCenterAndroidTheme {
                MainScreen(
                    this,
                    bottomNavItems = bottomBarItems,
                    selectedIndex = selectedIndex,
                    onItemSelected = { index ->
                        mainViewModel.setTabIndex(index)
                        mainViewModel.navigateToSelectedTab(navController)
                    },
                    navController = navController
                )
            }
        }


    }

}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MainScreen(
    activity: ComponentActivity,
    bottomNavItems:List<BottomNavItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    navController: NavHostController
)
{
    println("main Screen")

    Scaffold(
        bottomBar = {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Color.Transparent)
            )
            {
                BottomNavigationBar(
                    items = bottomNavItems,
                    selectedIndex = selectedIndex,
                    onItemSelected = onItemSelected
                )
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 62.dp)
        ) {
            NavHost(navController = navController, startDestination = "home") {

                composable("home") {
                    //training Id is provided by state hoisting
                    key("home"){
                        val homeViewModel: HomeViewModel =
                            (LocalContext.current as MainActivity).homeViewModel
                        HomeScreen(homeViewModel, navController) { trainingId ->
                            navController.navigate("details/$trainingId")
                        }
                    }

                }



                composable("trainings?startDate={startDateParam}&endDate={endDateParam}&fromHome={fromHome}") { backStackEntry ->

                    val startDateParam = backStackEntry.arguments?.getString("startDateParam")
                    val endDateParam = backStackEntry.arguments?.getString("endDateParam")
                    val fromHome = backStackEntry.arguments?.getString("fromHome") == "true"

                    val fetchAllAvailableTrainingsViewModel: FetchAllAvailableTrainingsViewModel =
                        (LocalContext.current as MainActivity).fetchAllAvailableTrainingsViewModel

                    TrainingScreen(
                        fetchAllAvailableTrainingsViewModel = fetchAllAvailableTrainingsViewModel,
                        navController,
                        startDateParam = if (fromHome) startDateParam else null,
                        endDateParam = if (fromHome) endDateParam else null,
                        fromHome =fromHome,
                        onTrainingItemClick = { trainingId ->
                            navController.navigate("details/$trainingId")
                        }
                    )
                }


                composable("details/{trainingId}") { backStackEntry ->
                    val trainingId = backStackEntry.arguments?.getString("trainingId")
                    val trainingDetailsViewModel: TrainingDetailsViewModel =
                        (LocalContext.current as MainActivity).trainingDetailsViewModel

                    if (trainingId != null) {
                        TrainingDetailsScreen(trainingDetailsViewModel, trainingId,navController)
                    }
                }
                composable("details/quizz/{quizzId}") { backStackEntry ->
                    val quizzId = backStackEntry.arguments?.getString("quizzId")
                    val quizzEvaluationViewModel: QuizzEvaluationViewModel =
                        (LocalContext.current as MainActivity).quizzEvaluationViewModel

                    if (quizzId != null) {
                        QuizzScreen(quizzEvaluationViewModel, quizzId,navController)
                    }
                }

                composable("profile") {
                    key("profile"){
                        val profileViewModel: ProfileViewModel =
                            (LocalContext.current as MainActivity).profileViewModel
                        ProfileScreen(profileViewModel, navController, activity)
                    }
                }
/*
                composable("settings") {
                    val settingsViewModel: SettingsViewModel =
                        (LocalContext.current as MainActivity).settingsViewModel
                    SettingsTab(settingsViewModel, activity)
                }
                */


                }
            }
        }
    }




