package epm.xnox.firebaseauth.presentation.signin.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import dagger.hilt.android.lifecycle.HiltViewModel
import epm.xnox.firebaseauth.core.Constant
import epm.xnox.firebaseauth.core.Response
import epm.xnox.firebaseauth.domain.usecase.auth.ForgotPasswordUseCase
import epm.xnox.firebaseauth.domain.usecase.auth.SignInWithCredentialUseCase
import epm.xnox.firebaseauth.domain.usecase.auth.SignInWithEmailAndPasswordUseCase
import epm.xnox.firebaseauth.presentation.common.FirebaseState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SigninViewModel @Inject constructor(
    private val signInWithEmailAndPasswordUseCase: SignInWithEmailAndPasswordUseCase,
    private val signInWithCredentialUseCase: SignInWithCredentialUseCase,
    private val forgotPasswordUseCase: ForgotPasswordUseCase
) : ViewModel() {

    private val _authState = mutableStateOf(FirebaseState())
    val authState: State<FirebaseState> = _authState

    private val _forgotPasswordState = mutableStateOf(FirebaseState())
    val forgotPasswordState: State<FirebaseState> = _forgotPasswordState

    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            signInWithEmailAndPasswordUseCase.invoke(email, password).collect { response ->
                when (response) {
                    Response.Loading ->
                        _authState.value = FirebaseState(loading = true)

                    is Response.Error ->
                        _authState.value = FirebaseState(error = response.message)

                    is Response.Success ->
                        _authState.value = FirebaseState(success = response.data)
                }
            }
        }
    }

    fun signInWithCredential(credential: Credential) {
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            viewModelScope.launch {
                signInWithCredentialUseCase.invoke(googleIdTokenCredential.idToken)
                    .collect { response ->
                        when (response) {
                            Response.Loading ->
                                _authState.value = FirebaseState(loading = true)

                            is Response.Error ->
                                _authState.value = FirebaseState(error = response.message)

                            is Response.Success ->
                                _authState.value = FirebaseState(success = response.data)
                        }
                    }
            }
        } else {
            _authState.value = FirebaseState(error = Constant.UNEXPECTED_CREDENTIAL)
        }
    }

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            forgotPasswordUseCase.invoke(email).collect { response ->
                when (response) {
                    Response.Loading ->
                        _forgotPasswordState.value = FirebaseState(loading = true)

                    is Response.Error ->
                        _forgotPasswordState.value = FirebaseState(error = response.message)

                    is Response.Success ->
                        _forgotPasswordState.value = FirebaseState(success = response.data)
                }
            }
        }
    }
}