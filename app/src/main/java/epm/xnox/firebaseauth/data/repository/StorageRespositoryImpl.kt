package epm.xnox.firebaseauth.data.repository

import com.google.firebase.storage.FirebaseStorage
import epm.xnox.firebaseauth.core.Constant
import epm.xnox.firebaseauth.core.Response
import epm.xnox.firebaseauth.domain.repository.StorageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageRespositoryImpl @Inject constructor(
    private val storage: FirebaseStorage
) : StorageRepository {

    override suspend fun uploadImage(uid: String, image: ByteArray): Flow<Response<String>> = flow {
        emit(Response.Loading)

        try {
            val reference =
                storage.reference.child("${Constant.STORAGE_CHILD_IMAGES}/$uid.jpg")
            val uploadTask = reference.putBytes(image).await()

            if (uploadTask.task.isSuccessful) {
                val downloadUrl = reference.downloadUrl.await()
                if (downloadUrl != null) {
                    emit(Response.Success(downloadUrl.toString()))
                } else {
                    emit(Response.Error(Constant.STORAGE_DOWNLOAD_URL_ERROR))
                }
            } else {
                emit(Response.Error(Constant.STORAGE_UPLOAD_ERROR))
            }

        } catch (e: Exception) {
            emit(Response.Error(e.message ?: Constant.UNKNOW_ERROR))
        }
    }
}