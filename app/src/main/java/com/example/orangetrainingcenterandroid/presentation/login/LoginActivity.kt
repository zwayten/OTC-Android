package com.example.orangetrainingcenterandroid.presentation.login


import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.orangetrainingcenterandroid.R
import com.example.orangetrainingcenterandroid.ui.theme.Orange500
import com.example.orangetrainingcenterandroid.ui.theme.OrangeTrainingCenterAndroidTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    @Inject
    lateinit var loginViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            OrangeTrainingCenterAndroidTheme {
                LoginScreen(loginViewModel = loginViewModel,this)
               /* if (loginViewModel.checkPermissionState.value == "NO_FLAG") {
                    ShowNotificationPermissionRequestDialog(
                        show = loginViewModel.checkPermissionState.value == "NO_FLAG",
                        onConfirm = {

                            loginViewModel.handlePermissionRequest(true)

                        },
                        onCancel = {
                            loginViewModel.handlePermissionRequest(false)
                        }
                    )
                }
                */
            }

        }
    }



}

@Composable
fun LoginScreen(loginViewModel: LoginViewModel,activity:LoginActivity) {
    val backgroundColor: Color = MaterialTheme.colors.background
    val textColor: Color = MaterialTheme.colors.onBackground
    val orangeColor: Color = MaterialTheme.colors.primary
    val surfaceColor: Color = MaterialTheme.colors.surface
    /*if (loginViewModel.checkPermissionState.value == "NO_FLAG") {
        ShowNotificationPermissionRequestDialog(
            show = loginViewModel.checkPermissionState.value == "NO_FLAG",
            onConfirm = {

                loginViewModel.handlePermissionRequest(true)

            },
            onCancel = {
                loginViewModel.handlePermissionRequest(false)
            }
        )
    }
    */

    val textStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = textColor,
        fontFamily = FontFamily(Font(R.font.neue_helvetica)),
        textAlign = TextAlign.Start
    )
    val titleTextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = MaterialTheme.typography.h4.fontSize,
        color = textColor,
        fontFamily = FontFamily(Font(R.font.neue_helvetica)),
        textAlign = TextAlign.Start
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
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
                .fillMaxWidth(0.5f).padding(bottom=30.dp)
                .wrapContentHeight().padding(bottom= 50.dp)
        )

        OutlinedTextField(
            value = loginViewModel.email.value,
            onValueChange = { loginViewModel.email.value = it },
            label = { Text("Email") },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = surfaceColor,
                textColor = textColor,
                focusedIndicatorColor = orangeColor,
                unfocusedIndicatorColor = textColor,
                focusedLabelColor = textColor,
                unfocusedLabelColor = textColor

            ),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth(0.9f)
        )
        OutlinedTextField(
            value = loginViewModel.password.value,
            onValueChange = { loginViewModel.password.value = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(0.9f),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = surfaceColor,
                textColor = textColor,
                focusedIndicatorColor = orangeColor,
                unfocusedIndicatorColor = textColor,
                focusedLabelColor = textColor,
                unfocusedLabelColor = textColor
            ),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            visualTransformation = PasswordVisualTransformation()
        )

        TextButton(
            onClick = {
                loginViewModel.navigateToResetPassword(activity)
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Forgot password?",
                style = textStyle)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { loginViewModel.loginUser() },
            modifier = Modifier.fillMaxWidth(0.9f).height(40.dp),
            enabled = true,
            colors = ButtonDefaults.buttonColors(backgroundColor = orangeColor)
        ) {

            if (loginViewModel.loginState.value == LoginViewModel.LoginState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
            } else {
                Text(
                    text = "Submit",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        when (val loginState = loginViewModel.loginState.value) {
            is LoginViewModel.LoginState.Error -> {
                Text(text = loginState.message, color = MaterialTheme.colors.error)
            }
            is LoginViewModel.LoginState.Success -> {
                loginViewModel.navigateToMain(activity)
            }
            is LoginViewModel.LoginState.Idle -> {

            }
            else -> {}
        }


    }
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














