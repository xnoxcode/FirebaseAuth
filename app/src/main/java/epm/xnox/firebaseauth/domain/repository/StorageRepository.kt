package epm.xnox.firebaseauth.domain.repository

import epm.xnox.firebaseauth.core.Response
import kotlinx.coroutines.flow.Flow

interface StorageRepository {
    suspend fun uploadImage(uid: String, image: ByteArray): Flow<Response<String>>
}