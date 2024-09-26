package epm.xnox.firebaseauth.presentation.profile.ui

import android.graphics.ImageDecoder
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import epm.xnox.firebaseauth.R
import epm.xnox.firebaseauth.presentation.profile.model.ProfileEvent
import epm.xnox.firebaseauth.presentation.profile.viewModel.ProfileViewModel
import epm.xnox.firebaseauth.ui.component.AuthTextField
import epm.xnox.firebaseauth.ui.component.ChargingDialog
import epm.xnox.firebaseauth.ui.component.DialogCustom
import epm.xnox.firebaseauth.ui.component.ProfileButton

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onNavigateToSignIn: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Header()
        Spacer(modifier = Modifier.height(15.dp))
        Body(viewModel, onNavigateToSignIn)
    }
}

@Composable
fun Header() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = stringResource(id = R.string.title_account),
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(id = R.string.signin_header_description),
            fontWeight = FontWeight.Light,
            color = Color.Black
        )
    }
}

@Composable
fun Body(viewModel: ProfileViewModel, onNavigateToSignIn: () -> Unit) {
    val context = LocalContext.current
    val userState = viewModel.userState.value
    val uploadImageState = viewModel.uploadImageState.value
    val updateUserState = viewModel.updateUserState.value
    val deleteAccountState = viewModel.deleteAccountState.value
    var editProfileState by remember { mutableStateOf(false) }
    var showDialogDeleteAccount by remember { mutableStateOf(false) }
    var currentPassword by remember { mutableStateOf("") }

    DialogCustom(
        show = showDialogDeleteAccount,
        content = {
            DeleteAccountContent(currentPassword) { value ->
                currentPassword = value
            }
        },
        onConfirmButton = {
            viewModel.deleteAccount(userState.uid, userState.email, currentPassword)
            showDialogDeleteAccount = false
        },
        onDismissButton = { showDialogDeleteAccount = false }
    )

    ChargingDialog(show = updateUserState.loading)
    ChargingDialog(show = deleteAccountState.loading)

    LaunchedEffect(uploadImageState.error) {
        if (uploadImageState.error.isNotBlank()) {
            Toast.makeText(context, uploadImageState.error, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(deleteAccountState.error) {
        if (deleteAccountState.error.isNotBlank()) {
            Toast.makeText(context, deleteAccountState.error, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(deleteAccountState.success) {
        if (deleteAccountState.success) {
            onNavigateToSignIn()
        }
    }

    LaunchedEffect(updateUserState.error) {
        if (updateUserState.error.isNotBlank()) {
            Toast.makeText(context, updateUserState.error, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(updateUserState.success) {
        if (updateUserState.success) {
            editProfileState = false
        }
    }

    val launcherTakeImage =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicturePreview()
        ) { bitmap ->
            bitmap?.let {
                viewModel.uploadImage(userState.uid, it)
            }
        }

    val launcherPickimage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        val source = uri?.let {
            ImageDecoder.createSource(context.contentResolver, it)
        }
        source?.let {
            viewModel.uploadImage(userState.uid, ImageDecoder.decodeBitmap(it))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BadgedBox(badge = {
                Icon(
                    painter = painterResource(id = R.drawable.outline_camera),
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        if (editProfileState) launcherTakeImage.launch(null)
                    }
                )
            }) {
                Box(contentAlignment = Alignment.Center) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(userState.image)
                            .crossfade(true)
                            .transformations()
                            .build(),
                        placeholder = painterResource(id = R.drawable.image_profile),
                        error = painterResource(id = R.drawable.image_profile),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier
                            .clip(CircleShape)
                            .border(
                                2.dp,
                                color = MaterialTheme.colorScheme.onBackground,
                                shape = CircleShape
                            )
                            .size(100.dp)
                            .clickable {
                                if (editProfileState) launcherPickimage.launch("image/*")
                            }
                    )
                    if (uploadImageState.loading) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = userState.email, color = MaterialTheme.colorScheme.surfaceVariant)
            Spacer(modifier = Modifier.height(15.dp))
            AuthTextField(
                modifier = Modifier.fillMaxWidth(0.8f),
                value = userState.user,
                label = stringResource(id = R.string.user_name_account),
                enabled = editProfileState,
                color = MaterialTheme.colorScheme.onBackground,
                leadingIcon = Icons.Outlined.Person,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            ) {
                viewModel.onEvent(ProfileEvent.OnNameChange(it))
            }
            Spacer(modifier = Modifier.height(15.dp))
            AuthTextField(
                modifier = Modifier.fillMaxWidth(0.8f),
                value = userState.info,
                label = stringResource(id = R.string.info_account),
                enabled = editProfileState,
                color = MaterialTheme.colorScheme.onBackground,
                leadingIcon = Icons.Outlined.Email,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            ) {
                viewModel.onEvent(ProfileEvent.OnInfoChange(it))
            }
            Spacer(modifier = Modifier.height(15.dp))
            Card(
                onClick = {
                    if (editProfileState) {
                        viewModel.updateUser(userState.uid)
                    } else {
                        editProfileState = true
                    }
                },
                colors = CardDefaults.cardColors(
                    contentColor = Color.White,
                    containerColor = Color.Black,
                    disabledContainerColor = Color.Black
                ),
                shape = RoundedCornerShape(18.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(vertical = 15.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(
                            id = if (editProfileState)
                                R.string.update_account
                            else
                                R.string.edit_account
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "O")
                Spacer(modifier = Modifier.width(10.dp))
                HorizontalDivider(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(15.dp))
            ProfileButton(
                label = stringResource(id = R.string.logout_account),
                icon = painterResource(id = R.drawable.ic_logout)
            ) {
                viewModel.logoutUser()
                onNavigateToSignIn()
            }
            Spacer(modifier = Modifier.height(15.dp))
            ProfileButton(
                label = stringResource(id = R.string.delete_account),
                icon = painterResource(id = R.drawable.ic_account_remove_outline)
            ) { showDialogDeleteAccount = true }
        }
    }
}

@Composable
fun DeleteAccountContent(value: String, onTextChange: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Red.copy(alpha = 0.5f)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(15.dp))
            Image(
                painter = painterResource(id = R.drawable.image_profile),
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    MaterialTheme.colorScheme.background
                )
            )
            Text(
                text = stringResource(id = R.string.dialog_delete_account_title),
                color = MaterialTheme.colorScheme.background
            )
            Spacer(modifier = Modifier.height(15.dp))
        }
        Spacer(modifier = Modifier.height(15.dp))
        Box {
            AuthTextField(
                modifier = Modifier.fillMaxWidth(0.8f),
                value = value,
                label = stringResource(id = R.string.signin_password),
                color = MaterialTheme.colorScheme.onBackground,
                leadingIcon = Icons.Outlined.Lock,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            ) { onTextChange(it) }
        }
    }
}