package epm.xnox.firebaseauth.core

import android.util.Patterns

fun String.isValidPassword(): Boolean =
    if (this.isBlank() || this.isEmpty()) false
    else if (this.length < 6) false
    else true

fun String.isValidEmail(): Boolean =
    if (this.isBlank() || this.isEmpty()) false
    else if (!Patterns.EMAIL_ADDRESS.matcher(this).matches()) false
    else true