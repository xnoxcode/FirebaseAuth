package epm.xnox.firebaseauth.domain.repository

import epm.xnox.firebaseauth.core.Response
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {
    suspend fun updateUser(uid: String, user: Map<String, String>): Flow<Response<Boolean>>
}