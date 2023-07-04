package com.example.orangetrainingcenterandroid.presentation.launch_screen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.orangetrainingcenterandroid.MainActivity
import com.example.orangetrainingcenterandroid.R
import com.example.orangetrainingcenterandroid.presentation.login.LoginActivity
import com.example.orangetrainingcenterandroid.ui.theme.OrangeTrainingCenterAndroidTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class LaunchScreenActivity : ComponentActivity() {
    @Inject
    lateinit var viewModel: LaunchScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            OrangeTrainingCenterAndroidTheme {
                LaunchScreen()
            }
        }



        lifecycleScope.launch {
            delay(2000)
            navigateToDestination(viewModel.isUserLoggedIn())
            finish()
        }
    }

    @Composable
    fun LaunchScreen() {
        val backgroundColor: Color = MaterialTheme.colors.background
        val textColor: Color = MaterialTheme.colors.onBackground
        val orangeColor: Color = MaterialTheme.colors.primary
        val surfaceColor: Color = MaterialTheme.colors.surface
        Box(
            modifier = Modifier
                .fillMaxSize(),

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
            Image(
                painter = painterResource(R.drawable.orange_logo),
                contentDescription = "Logo Orange",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .wrapContentHeight()
            )}

            val poweredByStyle = SpanStyle(
                fontWeight = FontWeight.Thin,
                fontSize = 16.sp,
                color = textColor,
                fontStyle = FontStyle.Normal,
                fontFamily = FontFamily(Font(R.font.neue_helvetica))
            )
            val orangeStyle = SpanStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                color = orangeColor,
                fontFamily = FontFamily(Font(R.font.neue_helvetica))
            )
            val digitalCenterStyle = SpanStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = textColor,
                fontFamily = FontFamily(Font(R.font.neue_helvetica))
            )

            val poweredByText = buildAnnotatedString {
                withStyle(poweredByStyle) {
                    append("Powered by \n")
                }
                withStyle(orangeStyle) {
                    append("Orange")
                }
                withStyle(digitalCenterStyle) {
                    append(" Digital Center")
                }
            }

            Text(
                text = poweredByText,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 16.dp),
                textAlign = TextAlign.Start,
                fontFamily = FontFamily(Font(R.font.neue_helvetica)),
                color = textColor
            )
        }
    }

    private fun navigateToDestination(isLoggedIn: Boolean) {

        if (isLoggedIn) {
            val intent = Intent(this@LaunchScreenActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this@LaunchScreenActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }


    }
}

