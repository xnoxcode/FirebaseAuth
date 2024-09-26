package epm.xnox.firebaseauth.data.mapper

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import epm.xnox.firebaseauth.core.Constant

fun FirebaseUser.toUser(
    provide: String, userName: String? = null, userEmail: String? = null
): Map<String, String> =
    mapOf(
        Constant.KEY_USER_UID to uid,
        Constant.KEY_USER_NAME to (displayName ?: userName ?: ""),
        Constant.KEY_USER_INFO to Constant.FIRESTORE_CHILD_INFO_DEFAULT,
        Constant.KEY_USER_EMAIL to (email ?: userEmail ?: ""),
        Constant.KEY_USER_IMAGE to photoUrl.toString(),
        Constant.KEY_USER_PROVIDE to provide
    )

fun DocumentSnapshot.toUser(): Map<String, String> =
    mapOf(
        Constant.KEY_USER_UID to (getString(Constant.KEY_USER_UID) ?: ""),
        Constant.KEY_USER_NAME to (getString(Constant.KEY_USER_NAME) ?: ""),
        Constant.KEY_USER_INFO to (getString(Constant.KEY_USER_INFO) ?: ""),
        Constant.KEY_USER_EMAIL to (getString(Constant.KEY_USER_EMAIL) ?: ""),
        Constant.KEY_USER_IMAGE to (getString(Constant.KEY_USER_IMAGE) ?: ""),
        Constant.KEY_USER_PROVIDE to (getString(Constant.KEY_USER_PROVIDE) ?: "")
    )