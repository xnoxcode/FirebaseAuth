package epm.xnox.firebaseauth.presentation.common

data class FirebaseState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String = ""
)
