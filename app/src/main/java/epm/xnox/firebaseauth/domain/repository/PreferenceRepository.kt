package epm.xnox.firebaseauth.domain.repository

import epm.xnox.firebaseauth.domain.model.User
import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {
    suspend fun readUserData(): Flow<User>
    suspend fun saveUserData(user: Map<String, String>)
    suspend fun clearUserData()
}