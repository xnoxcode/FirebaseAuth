package epm.xnox.firebaseauth.presentation.profile.viewModel

import android.graphics.Bitmap
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import epm.xnox.firebaseauth.core.Response
import epm.xnox.firebaseauth.domain.model.User
import epm.xnox.firebaseauth.domain.usecase.common.ConvertBitmapToByteArrayUseCase
import epm.xnox.firebaseauth.domain.usecase.preference.SaveUserDataUseCase
import epm.xnox.firebaseauth.domain.usecase.profile.ClearUserDataUseCase
import epm.xnox.firebaseauth.domain.usecase.profile.DeleteAccountUseCase
import epm.xnox.firebaseauth.domain.usecase.profile.LogoutUserUseCase
import epm.xnox.firebaseauth.domain.usecase.profile.ReadUserDataUseCase
import epm.xnox.firebaseauth.domain.usecase.profile.UpdateUserUseCase
import epm.xnox.firebaseauth.domain.usecase.profile.UploadImageUseCase
import epm.xnox.firebaseauth.presentation.common.FirebaseState
import epm.xnox.firebaseauth.presentation.profile.model.ProfileEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val logutUserUseCase: LogoutUserUseCase,
    private val readUserDataUseCase: ReadUserDataUseCase,
    private val uploadImageUseCase: UploadImageUseCase,
    private val convertBitmapToByteArrayUseCase: ConvertBitmapToByteArrayUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val saveUserDataUseCase: SaveUserDataUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val clearUserDataUseCase: ClearUserDataUseCase,
) : ViewModel() {

    private var _userState = mutableStateOf(User())
    val userState: State<User> = _userState

    private var _uploadImageState = mutableStateOf(FirebaseState())
    val uploadImageState: State<FirebaseState> = _uploadImageState

    private var _updateUserState = mutableStateOf(FirebaseState())
    val updateUserState: State<FirebaseState> = _updateUserState

    private var _deleteAccountState = mutableStateOf(FirebaseState())
    val deleteAccountState: State<FirebaseState> = _deleteAccountState

    init {
        readUserData()
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.OnInfoChange ->
                _userState.value = _userState.value.copy(info = event.value)

            is ProfileEvent.OnNameChange ->
                _userState.value = _userState.value.copy(user = event.value)
        }
    }

    fun logoutUser() {
        viewModelScope.launch {
            logutUserUseCase.invoke()
        }
    }

    fun uploadImage(uid: String, image: Bitmap) {
        val imageByteArray = convertBitmapToByteArrayUseCase.invoke(image)

        if (imageByteArray != null) {
            viewModelScope.launch {
                uploadImageUseCase.invoke(uid, imageByteArray).collect { response ->
                    when (response) {
                        Response.Loading ->
                            _uploadImageState.value = FirebaseState(loading = true)

                        is Response.Error ->
                            _uploadImageState.value = FirebaseState(error = response.message)

                        is Response.Success -> {
                            _uploadImageState.value = FirebaseState(success = true)
                            _userState.value = _userState.value.copy(image = response.data)
                        }
                    }
                }
            }
        }
    }

    fun updateUser(uid: String) {
        viewModelScope.launch {
            updateUserUseCase.invoke(uid, _userState.value).collect { response ->
                when (response) {
                    Response.Loading ->
                        _updateUserState.value = FirebaseState(loading = true)

                    is Response.Error ->
                        _updateUserState.value = FirebaseState(error = response.message)

                    is Response.Success -> {
                        saveUserDataUseCase.invoke(_userState.value)
                        _updateUserState.value = FirebaseState(success = response.data)
                    }
                }
            }
        }
    }

    fun deleteAccount(uid: String, email: String, password: String) {
        viewModelScope.launch {
            deleteAccountUseCase.invoke(uid, email, password).collect { response ->
                when (response) {
                    Response.Loading ->
                        _deleteAccountState.value = FirebaseState(
                            loading = true
                        )

                    is Response.Error ->
                        _deleteAccountState.value = FirebaseState(error = response.message)

                    is Response.Success -> {
                        clearUserDataUseCase.invoke()
                        _deleteAccountState.value = FirebaseState(success = response.data)
                    }
                }
            }
        }
    }

    private fun readUserData() {
        viewModelScope.launch {
            readUserDataUseCase.invoke().collect { user ->
                _userState.value = user
            }
        }
    }
}