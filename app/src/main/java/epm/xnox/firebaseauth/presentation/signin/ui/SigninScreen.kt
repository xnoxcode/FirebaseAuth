package epm.xnox.firebaseauth.presentation.signin.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import epm.xnox.firebaseauth.R
import epm.xnox.firebaseauth.core.isValidEmail
import epm.xnox.firebaseauth.core.isValidPassword
import epm.xnox.firebaseauth.presentation.signin.viewModel.SigninViewModel
import epm.xnox.firebaseauth.ui.component.AuthButton
import epm.xnox.firebaseauth.ui.component.AuthTextField
import epm.xnox.firebaseauth.ui.component.ChargingDialog
import epm.xnox.firebaseauth.ui.component.DialogCustom

@Composable
fun SigninScreen(
    viewModel: SigninViewModel,
    onNavigateToRegister: () -> Unit,
    onNavigateToBack: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Header(onNavigateToBack, onNavigateToRegister)
        Spacer(modifier = Modifier.height(15.dp))
        Body(viewModel, onNavigateToProfile)
    }
}

@Composable
fun Header(onNavigateToBack: () -> Unit, onNavigateToRegister: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { onNavigateToBack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = null,
                    tint = Color.Black
                )
            }
            TextButton(
                onClick = { onNavigateToRegister() }, colors = ButtonDefaults.buttonColors(
                    contentColor = Color.Black, containerColor = Color.Transparent
                )
            ) {
                Text(
                    text = stringResource(id = R.string.signin_header_register),
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = stringResource(id = R.string.sign_in),
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
fun Body(viewModel: SigninViewModel, onNavigateToProfile: () -> Unit) {
    var emailState by remember { mutableStateOf("") }
    var passwordState by remember { mutableStateOf("") }
    var forgotPasswordEmailValue by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var showDialogForgotPassword by remember { mutableStateOf(false) }
    var showDialogForgotPasswordSuccess by remember { mutableStateOf(false) }
    val authState = viewModel.authState.value
    val forgotPasswordState = viewModel.forgotPasswordState.value
    val context = LocalContext.current

    ChargingDialog(show = authState.loading)
    ChargingDialog(show = forgotPasswordState.loading)

    DialogCustom(
        title = stringResource(id = R.string.dialog_forgot_password_success_title),
        show = showDialogForgotPasswordSuccess,
        content = {
            Box(modifier = Modifier.padding(horizontal = 15.dp)) {
                Text(text = stringResource(id = R.string.dialog_forgot_password_success_content))
            }
        },
        onConfirmButton = { showDialogForgotPasswordSuccess = false })

    DialogCustom(
        show = showDialogForgotPassword,
        content = {
            ForgotPasswordtContent(value = forgotPasswordEmailValue) { value ->
                forgotPasswordEmailValue = value
            }
        },
        onConfirmButton = {
            viewModel.forgotPassword(forgotPasswordEmailValue)
            showDialogForgotPassword = false
        },
        onDismissButton = {
            showDialogForgotPassword = false
        }
    )

    LaunchedEffect(authState.error) {
        if (authState.error.isNotBlank()) {
            Toast.makeText(context, authState.error, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(forgotPasswordState.error) {
        if (forgotPasswordState.error.isNotBlank()) {
            Toast.makeText(context, forgotPasswordState.error, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(authState.success) {
        if (authState.success) {
            onNavigateToProfile()
        }
    }

    LaunchedEffect(forgotPasswordState.success) {
        if (forgotPasswordState.success) {
            showDialogForgotPasswordSuccess = true
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
            AuthTextField(
                modifier = Modifier.fillMaxWidth(0.8f),
                value = emailState,
                error = stringResource(id = R.string.message_error_email),
                isError = emailError,
                label = stringResource(id = R.string.signin_email),
                color = MaterialTheme.colorScheme.onBackground,
                leadingIcon = Icons.Outlined.Person,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            ) {
                emailState = it
                emailError = !it.isValidEmail()
            }

            Spacer(modifier = Modifier.height(15.dp))

            AuthTextField(
                modifier = Modifier.fillMaxWidth(0.8f),
                value = passwordState,
                error = stringResource(id = R.string.message_error_password),
                isError = passwordError,
                label = stringResource(id = R.string.signin_password),
                color = MaterialTheme.colorScheme.onBackground,
                leadingIcon = Icons.Outlined.Lock,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            ) {
                passwordState = it
                passwordError = !it.isValidPassword()
            }

            Spacer(modifier = Modifier.height(5.dp))

            Box(
                modifier = Modifier.fillMaxWidth(0.8f), contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    modifier = Modifier.clickable { showDialogForgotPassword = true },
                    text = stringResource(id = R.string.signin_forgot_password),
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            Card(
                onClick = {
                    if (emailState.isValidEmail() && passwordState.isValidPassword()) viewModel.signInWithEmailAndPassword(
                        emailState,
                        passwordState
                    )
                }, colors = CardDefaults.cardColors(
                    contentColor = Color.White, containerColor = Color.Black
                ), shape = RoundedCornerShape(18.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(vertical = 15.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = stringResource(id = R.string.sign_in), color = Color.White)
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
            AuthButton(
                icon = painterResource(id = R.drawable.google_logo), label = stringResource(
                    id = R.string.signin_with_google
                ),
                autoSelectedEnabled = true,
                filterByAuthorizedAccounts = true
            ) { credential ->
                viewModel.signInWithCredential(credential)
            }
        }
    }
}

@Composable
fun ForgotPasswordtContent(value: String, onTextChange: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.surfaceVariant),
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
                text = stringResource(id = R.string.dialog_forgot_password_title),
                color = MaterialTheme.colorScheme.background
            )
            Spacer(modifier = Modifier.height(15.dp))
        }
        Spacer(modifier = Modifier.height(15.dp))
        Box {
            AuthTextField(
                modifier = Modifier.fillMaxWidth(0.8f),
                value = value,
                label = stringResource(id = R.string.signin_email),
                color = MaterialTheme.colorScheme.onBackground,
                leadingIcon = Icons.Outlined.Email,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            ) { onTextChange(it) }
        }
    }
}
