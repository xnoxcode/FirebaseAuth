package epm.xnox.firebaseauth.domain.repository

import epm.xnox.firebaseauth.core.Response
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signUpWithEmailAndPassword(
        user: String,
        email: String,
        password: String
    ): Flow<Response<Boolean>>

    suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Response<Boolean>>

    suspend fun signInWithCredential(idToken: String): Flow<Response<Boolean>>

    suspend fun deleteAccount(uid: String, email: String, password: String): Flow<Response<Boolean>>

    suspend fun forgotPassword(email: String): Flow<Response<Boolean>>

    suspend fun logoutUser()
}