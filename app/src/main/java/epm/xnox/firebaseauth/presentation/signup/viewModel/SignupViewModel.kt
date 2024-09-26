package epm.xnox.firebaseauth.presentation.signup.viewModel

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
import epm.xnox.firebaseauth.domain.usecase.auth.SignInWithCredentialUseCase
import epm.xnox.firebaseauth.domain.usecase.auth.SignUpWithEmailAndPasswordUseCase
import epm.xnox.firebaseauth.presentation.common.FirebaseState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val signUpWithEmailAndPasswordUseCase: SignUpWithEmailAndPasswordUseCase,
    private val signInWithCredentialUseCase: SignInWithCredentialUseCase
) : ViewModel() {

    private val _authState = mutableStateOf(FirebaseState())
    val authState: State<FirebaseState> = _authState

    fun signUpWithEmailAndPassword(user: String, email: String, password: String) {
        viewModelScope.launch {
            signUpWithEmailAndPasswordUseCase.invoke(user, email, password).collect { response ->
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
}