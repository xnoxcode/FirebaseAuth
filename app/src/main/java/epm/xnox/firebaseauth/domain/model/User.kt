package epm.xnox.firebaseauth.domain.model

data class User(
    val uid: String,
    val user: String,
    val info: String,
    val email: String,
    val image: String,
    val provide: String
) {
    constructor(): this("","", "", "", "", "")
}
