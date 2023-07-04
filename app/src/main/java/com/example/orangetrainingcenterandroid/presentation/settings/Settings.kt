package com.example.orangetrainingcenterandroid.presentation.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.orangetrainingcenterandroid.MainActivity
import com.example.orangetrainingcenterandroid.R
import com.example.orangetrainingcenterandroid.presentation.login.LoginActivity
import com.example.orangetrainingcenterandroid.ui.theme.Orange500
import com.example.orangetrainingcenterandroid.ui.theme.White

@Composable
fun SettingsTab(viewModel: SettingsViewModel, activity: ComponentActivity) {
    Column(modifier = Modifier.fillMaxSize()) {
        //ThemeRow()
        LogoutRow(onConfirm = {
            viewModel.logout()
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            activity.startActivity(intent)
            activity.finish()
        })
    }
}

@Composable
fun LogoutRow(onConfirm: () -> Unit) {
    val showDialog = remember { mutableStateOf(false) }
    val textStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = White,
        fontFamily = FontFamily(Font(R.font.neue_helvetica)),
        textAlign = TextAlign.Start
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp)
    ) {
        /*  Icon(
             modifier = Modifier.size(24.dp),
             imageVector = Icons.Default.Logout,
             contentDescription = "Logout"
         )

        Text(
             text = "Logout",style=textStyle,
             modifier = Modifier.padding(start = 8.dp)
         )*/

        Spacer(modifier = Modifier.weight(1f))

        Button(onClick = { showDialog.value = true }) {
            Text(text = "Logout", style= textStyle)
        }
    }

    ShowLogoutConfirmationDialog(
        show = showDialog.value,
        onConfirm = onConfirm,
        onCancel = { showDialog.value = false }
    )
}

@Composable
fun ShowLogoutConfirmationDialog(
    show: Boolean,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    val textStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = Color.Black,
        fontFamily = FontFamily(Font(R.font.neue_helvetica)),
        textAlign = TextAlign.Start
    )
    if (show) {
        AlertDialog(
            onDismissRequest = { onCancel() },
            title = { Text(text = "Confirm Logout",style=textStyle) },
            text = { Text(text = "Are you sure you want to logout?",style=textStyle) },
            confirmButton = {
                Button(
                    onClick = { onConfirm() },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Orange500)
                ) {
                    Text(
                        text = "Logout",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onCancel() }
                ) {
                    Text(
                        text = "Cancel",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            color = Orange500
                        )
                    )
                }
            }
        )
    }
}


@Composable
fun ThemeRow() {
    var isSwitchOn by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp)
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = if (isSwitchOn) Icons.Default.Nightlight else Icons.Default.LightMode,
            contentDescription = "Icon",
            tint = if (isSwitchOn) Orange500 else Color.Black
        )

        Text(
            text = "Dark mode",
            modifier = Modifier.padding(start = 8.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        ThemeSwitcher(
            darkTheme = isSwitchOn,
            onThemeSwitched = { isChecked ->
                isSwitchOn = isChecked
            }
        )
    }
}

@Composable
fun ThemeSwitcher(
    darkTheme: Boolean,
    onThemeSwitched: (Boolean) -> Unit
) {
    Switch(
        checked = darkTheme,
        onCheckedChange = { isChecked ->
            onThemeSwitched(isChecked)
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color.LightGray,
            checkedTrackColor = Orange500,

            uncheckedThumbColor = Orange500,
            uncheckedTrackColor = Color.LightGray,
        ),
    )
}


