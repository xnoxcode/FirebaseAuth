package epm.xnox.firebaseauth.domain.usecase.common

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class ConvertBitmapToByteArrayUseCase @Inject constructor() {
    operator fun invoke(bitmap: Bitmap?): ByteArray? {
        return try {
            val baos = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            baos.toByteArray()
        } catch (e: Exception) {
            null
        }
    }
}