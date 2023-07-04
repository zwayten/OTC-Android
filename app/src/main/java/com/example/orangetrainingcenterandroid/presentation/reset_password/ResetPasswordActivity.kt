package com.example.orangetrainingcenterandroid.presentation.reset_password

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint.Align
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Start
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.orangetrainingcenterandroid.R
import com.example.orangetrainingcenterandroid.presentation.login.LoginActivity
import com.example.orangetrainingcenterandroid.ui.theme.OrangeTrainingCenterAndroidTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ResetPasswordActivity : ComponentActivity() {

    @Inject
    lateinit var resetPasswordViewModel: ResetPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrangeTrainingCenterAndroidTheme{ ResetPassword(resetPasswordViewModel, this) }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        navigateBackToLogin()
    }
    private fun navigateBackToLogin() {
        val intent = Intent(this@ResetPasswordActivity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

        @SuppressLint("NotConstructor", "SuspiciousIndentation")
        @Composable
        fun ResetPassword(resetPasswordViewModel: ResetPasswordViewModel, activity: ResetPasswordActivity) {
            val backgroundColor: Color = MaterialTheme.colors.background
            val textColor: Color = MaterialTheme.colors.onBackground
            val orangeColor: Color = MaterialTheme.colors.primary
            val surfaceColor: Color = MaterialTheme.colors.surface
            val textStyle = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
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
                        .fillMaxWidth(0.5f)
                        .padding(bottom = 30.dp)
                        .wrapContentHeight()
                        .padding(bottom = 50.dp)
                )

                Column(modifier = Modifier.fillMaxWidth(1f),
                horizontalAlignment = Alignment.CenterHorizontally){
                    Text(text = "A recovery email will be sent to this address", style = textStyle, modifier = Modifier.padding(bottom=10.dp))
                    OutlinedTextField(
                        value = resetPasswordViewModel.email.value,
                        onValueChange = { resetPasswordViewModel.email.value = it },
                        label = { Text("Email") },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = surfaceColor,
                            textColor = textColor,
                            focusedIndicatorColor = orangeColor,
                            unfocusedIndicatorColor = textColor,
                            focusedLabelColor = textColor,
                            unfocusedLabelColor = textColor
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        modifier = Modifier.fillMaxWidth(0.9f).padding(bottom = 10.dp)
                    )
                }

                Button(
                    onClick = { resetPasswordViewModel.resetPassword() },
                    modifier = Modifier.fillMaxWidth(0.5f),
                    enabled = resetPasswordViewModel.isResetPasswordButtonEnabled.value,
                    colors = ButtonDefaults.buttonColors(backgroundColor = orangeColor)
                ) {
                    val buttonText = if (resetPasswordViewModel.isResetPasswordButtonEnabled.value) {
                        "Send"
                    } else {
                        "Resend in ${resetPasswordViewModel.resetPasswordButtonTimer.value} s"
                    }

                    Text(
                        text = buttonText,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            color = if (resetPasswordViewModel.isResetPasswordButtonEnabled.value) Color.White else Color.Black
                        )
                    )
                }
                if(!resetPasswordViewModel.emailError.value.isEmpty())
                Text(text = resetPasswordViewModel.emailError.value, color = MaterialTheme.colors.error)
            }
        }
        }
}