package com.example.orangetrainingcenterandroid.presentation.profile


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.orangetrainingcenterandroid.R
import com.example.orangetrainingcenterandroid.ui.theme.Orange500
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.orangetrainingcenterandroid.presentation.login.LoginActivity
import com.google.accompanist.permissions.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch

enum class PermissionStatus {
    Granted,
    Denied,
    NotDetermined
}

suspend fun checkPermissionStatus(permission: String, context: Context): PermissionStatus {
    val permissionStatus = ContextCompat.checkSelfPermission(context, permission)
    return when {
        permissionStatus == PackageManager.PERMISSION_GRANTED -> PermissionStatus.Granted
        ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permission) -> PermissionStatus.Denied
        else -> PermissionStatus.NotDetermined
    }
}
@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel, navController: NavController,activity: ComponentActivity) {

    println("Profile screen")

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var isPasswordDialogOpen by remember { mutableStateOf(false) }
    var isRequestDialogOpen by remember { mutableStateOf(false) }
    var isProfileUpdateDialogOpen by remember { mutableStateOf(false) }
    var isShowLogoutConfirmationDialog by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }


    val scrollState = rememberScrollState()

    val errorMessage = profileViewModel.errorMessage.value

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }
    profileViewModel.loadProfile()

    val fullName: String by profileViewModel.fullName.collectAsState("")
    val phoneNumber: String by profileViewModel.phoneNumber.collectAsState("")
    val user: String by profileViewModel.id.collectAsState("")

    var isNotificationsEnabled by remember { mutableStateOf(false) }
    val permissionState = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

    var hasNotifPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    isNotificationsEnabled = hasNotifPermission


    ShowLogoutConfirmationDialog(
        show = isShowLogoutConfirmationDialog,
        onConfirm = {
            profileViewModel.logout()
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            activity.startActivity(intent)
            activity.finish()},
        onCancel = { isShowLogoutConfirmationDialog = false }
    )


    NotificationSettingsDialog(showDialog, { showDialog = false }, context)

    if (isPasswordDialogOpen) {
        PasswordDialog(
            onSave = { currentPassword, newPassword ->
                profileViewModel.updatePassword(currentPassword, newPassword)
            },
            onCancel = {
                isPasswordDialogOpen = false
                profileViewModel.resetErrorMessage()},
            errorMessage = errorMessage
        )
    }
    if (isRequestDialogOpen) {
        RequestDialog(
            onSave = { description ->
                profileViewModel.sendRequest(description)
            },
            onCancel = {
                isRequestDialogOpen = false
                profileViewModel.resetErrorMessage()},
            errorMessage = errorMessage
        )
    }

    if (isProfileUpdateDialogOpen) {
        ProfileUpdateDialog(
            onSave = { fullname, phoneNumber ->
                profileViewModel.updateProfile(fullname, phoneNumber)
                profileViewModel.loadProfile()
            },
            onCancel = {
                isProfileUpdateDialogOpen = false
                profileViewModel.resetErrorMessage()},

            fullName,
            phoneNumber
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize(1f)
            .background(Orange500),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(1f)
                .wrapContentHeight()
                .padding(top = 10.dp, bottom = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {

            ProfileItem(profileViewModel)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(1f)
                .wrapContentHeight()
        ) {
            Card(
                modifier = Modifier

                    .fillMaxSize(1f)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),


                backgroundColor = Color.White,

                ) {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .verticalScroll(scrollState)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(Color.LightGray)
                            .padding(10.dp)
                    ) {

                        Text(
                            text = "Profile Settings",
                            modifier = Modifier.padding(start = 8.dp),
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .clickable { isPasswordDialogOpen = true }
                            .padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Password",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Update Password",
                            modifier = Modifier.padding(start = 8.dp),
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .clickable { isProfileUpdateDialogOpen = true }
                            .padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ModeEdit,
                            contentDescription = "Second Icon",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Update Profile Informations",
                            modifier = Modifier.padding(start = 8.dp),
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                    }
                    //other settings
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(Color.LightGray)
                            .padding(10.dp)
                    ) {

                        Text(
                            text = "Other Settings",
                            modifier = Modifier.padding(start = 8.dp),
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .clickable { isRequestDialogOpen = true }
                            .padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AttachEmail,
                            contentDescription = "",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Make a Request",
                            modifier = Modifier.padding(start = 8.dp),
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val notificationIcon = if (isNotificationsEnabled) {
                            Icons.Default.NotificationsActive
                        } else {
                            Icons.Default.NotificationsOff
                        }

                        Icon(
                            imageVector = notificationIcon,
                            contentDescription = "Notification Icon",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Notifications",
                            modifier = Modifier.padding(start = 8.dp),
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Switch(
                            checked = isNotificationsEnabled,
                            onCheckedChange = { isChecked ->
                                isNotificationsEnabled = isChecked
                                if (isChecked) {
                                    showDialog = true
                                    // profileViewModel.subscribeToTopic(user)
                                } else {
                                    // profileViewModel.unsubscribeFromTopic(user)
                                    showDialog = true
                                }
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.LightGray,
                                checkedTrackColor = Orange500,
                                uncheckedThumbColor = Orange500,
                                uncheckedTrackColor = Color.LightGray
                            )
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .clickable { }
                            .padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "About",
                            modifier = Modifier.padding(start = 8.dp),
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .clickable { isShowLogoutConfirmationDialog = true }
                            .padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Logout",
                            modifier = Modifier.padding(start = 8.dp, bottom = 20.dp),
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                    }




                    // Add more rows with different click behavior as needed
                }
            }


        }

    }
}

@Composable
fun ProfileItem(profileViewModel: ProfileViewModel) {

    val fullName: String by profileViewModel.fullName.collectAsState("")
    val email: String by profileViewModel.email.collectAsState("")
    val phoneNumber: String by profileViewModel.phoneNumber.collectAsState("")
    val direction: String by profileViewModel.direction.collectAsState("")
    val department: String by profileViewModel.departement.collectAsState("")
    val role: String by profileViewModel.role.collectAsState("")
    val gender: String by profileViewModel.gender.collectAsState("")
    val customTextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        fontFamily = FontFamily(Font(R.font.neue_helvetica)),
        textAlign = TextAlign.Start,
        color = Color.Black
    )
    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .wrapContentHeight()
            //.background(Color.Black.copy(alpha = 0.85f))
            //.blur(8.dp)
            .clip(RoundedCornerShape(10.dp)),
        //.blur(1000.dp, 50.dp),
        elevation = 20.dp,
        backgroundColor = Color.White.copy(alpha = 1f),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier
            .wrapContentHeight()
            .padding(10.dp)) {

            ProfileItem(
                icon = Icons.Default.Person,
                text = fullName
            )
            ProfileItemEmail(
                icon = Icons.Default.Email,
                text = email
            )
            ProfileItem(
                icon = Icons.Default.Phone,
                text = phoneNumber
            )
            ProfileItem(
                icon = Icons.Default.LocationOn,
                text = direction
            )
            ProfileItem(
                icon = Icons.Default.Face,
                text = gender
            )
            ProfileItem(
                icon = Icons.Default.Business,
                text = department
            )
            ProfileItem(
                icon = Icons.Default.Work,
                text = role
            )


        }
    }
}


@Composable
fun ProfileItem(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
            .fillMaxWidth()
            .wrapContentHeight()
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
        modifier = Modifier
            .padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
            .fillMaxWidth()
            .wrapContentHeight()
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
fun PasswordDialog(onSave: (String, String) -> Unit, onCancel: () -> Unit, errorMessage: String) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var showCurrentPassword by remember { mutableStateOf(false) }


    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .width(320.dp)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Change Password",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
                OutlinedTextField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    label = { Text("Current Password") },
                    visualTransformation = if (showCurrentPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showCurrentPassword = !showCurrentPassword }) {
                            Icon(
                                painter = painterResource(id = if (showCurrentPassword) R.drawable.show else R.drawable.hide),
                                contentDescription = "Toggle Current Password Visibility",
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        textColor = Color.Black,
                        focusedIndicatorColor = Orange500,
                        unfocusedIndicatorColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Gray
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("New Password") },
                    visualTransformation = if (showCurrentPassword) VisualTransformation.None else PasswordVisualTransformation(),

                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        textColor = Color.Black,
                        focusedIndicatorColor = Orange500,
                        unfocusedIndicatorColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Gray
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp) // Adjust the height as needed
                ) {
                    if (errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            style = MaterialTheme.typography.body2,
                            color = Color.Red,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .align(Alignment.CenterStart)
                        )
                    }
                }
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.BottomEnd // Adjust the vertical alignment as needed
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = onCancel,
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                        ) {
                            Text(
                                "Cancel",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                        TextButton(
                            onClick = { onSave(currentPassword, newPassword)
                                      },
                            enabled = currentPassword.isNotBlank() && newPassword.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Orange500)
                        ) {
                            Text(
                                "Save",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RequestDialog(onSave: ( String) -> Unit, onCancel: () -> Unit, errorMessage: String) {
    var description by remember { mutableStateOf("") }



    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth(1f)
                .fillMaxHeight(0.7f)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.width(300.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Send Request",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .fillMaxHeight(0.7f)
                        .padding(16.dp),
                    textStyle = TextStyle(fontSize = 16.sp),
                    maxLines = Int.MAX_VALUE,
                    singleLine = false
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .height(32.dp) // Adjust the height as needed
                ) {
                    if (errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            style = MaterialTheme.typography.body2,
                            color = Color.Red,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .align(Alignment.CenterStart)
                        )
                    }
                }
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.BottomEnd // Adjust the vertical alignment as needed
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = onCancel,
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                        ) {
                            Text(
                                "Cancel",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                        TextButton(
                            onClick = { onSave(description)
                                      onCancel()},
                            enabled = description.isNotBlank() ,
                            colors = ButtonDefaults.buttonColors(backgroundColor = Orange500)
                        ) {
                            Text(
                                "Send",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}




@Composable
fun ProfileUpdateDialog(onSave: (String, String) -> Unit, onCancel: () -> Unit, fullName:String, phoneNumber: String) {
    var updatedFullName by remember { mutableStateOf(fullName) }
    var updatedPhoneNumber by remember { mutableStateOf(phoneNumber) }

    val isFullNameValid = updatedFullName.isNotEmpty()
    val isPhoneNumberValid = updatedPhoneNumber.length == 8
    var errorMessage by remember { mutableStateOf("") }
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .width(320.dp)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Update Profile",
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
                OutlinedTextField(
                    value = updatedFullName,
                    onValueChange = { updatedFullName = it },
                    label = { Text("current fullname") },


                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        textColor = Color.Black,
                        focusedIndicatorColor = Orange500,
                        unfocusedIndicatorColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Gray
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )
                OutlinedTextField(
                    value = updatedPhoneNumber,
                    onValueChange = { updatedPhoneNumber = it },
                    label = { Text("Phone Number") },


                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        textColor = Color.Black,
                        focusedIndicatorColor = Orange500,
                        unfocusedIndicatorColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Gray
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp) // Adjust the height as needed
                ) {
                    if (errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            style = MaterialTheme.typography.body2,
                            color = Color.Red,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .align(Alignment.CenterStart)
                        )
                    }
                }
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.BottomEnd // Adjust the vertical alignment as needed
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = onCancel,
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                        ) {
                            Text(
                                "Cancel",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                        TextButton(
                            onClick = {
                                if (isFullNameValid && isPhoneNumberValid) {
                                    onSave(updatedFullName, updatedPhoneNumber)
                                    onCancel() // Close the dialog
                                } else if(!isFullNameValid){
                                    errorMessage = "Fullname cannot be empty"
                                } else if(!isPhoneNumberValid){
                                    errorMessage = "Phone numeber must contain 8 numbers"
                                } else if(!isFullNameValid && !isPhoneNumberValid){
                                    errorMessage = "Full name and phone number are not valid"
                                }
                            },

                            colors = ButtonDefaults.buttonColors(backgroundColor = Orange500)
                        ) {
                            Text(
                                "Save",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                    }
                }
            }
        }
    }
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
fun PermissionAlertDialog(
    onPermissionRequested: () -> Unit,
    onSettingsClicked: () -> Unit,
    onDismiss: () -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        onPermissionRequested.invoke()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Permission Required") },
        text = {
            Column {
                Text("This app requires notification permission.")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Please grant the permission in the app settings.")
            }
        },
        confirmButton = {
            Button(
                onClick = { launcher.launch(Manifest.permission.POST_NOTIFICATIONS) },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
            ) {
                Text("Open Settings")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
            ) {
                Text("Dismiss")
            }
        }
    )
}

@Composable
fun NotificationSettingsDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    context:Context
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(text = "Notification Settings") },
            text = {
                Column {
                    Text(text = "Manage your app notifications by adjusting settings.")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { openAppSettings(context) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Open App Settings")
                    }
                }
            },
            confirmButton = {},
            dismissButton = {}
        )
    }
}
private fun openAppSettings(context: Context) {

    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
    intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
    context.startActivity(intent)
}