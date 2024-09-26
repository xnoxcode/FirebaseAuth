package epm.xnox.firebaseauth.presentation.profile.model

sealed class ProfileEvent {
    data class OnNameChange(val value: String): ProfileEvent()
    data class OnInfoChange(val value: String): ProfileEvent()
}