package epm.xnox.firebaseauth.domain.mappper

import epm.xnox.firebaseauth.core.Constant
import epm.xnox.firebaseauth.domain.model.User

fun User.toUser(): Map<String, String> =
    mapOf(
        Constant.KEY_USER_NAME to user,
        Constant.KEY_USER_INFO to info,
        Constant.KEY_USER_EMAIL to email,
        Constant.KEY_USER_IMAGE to image,
    )