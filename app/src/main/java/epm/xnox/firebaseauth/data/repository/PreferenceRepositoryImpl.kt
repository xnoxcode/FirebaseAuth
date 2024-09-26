package epm.xnox.firebaseauth.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import epm.xnox.firebaseauth.core.Constant
import epm.xnox.firebaseauth.domain.model.User
import epm.xnox.firebaseauth.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = Constant.PREFERENCE_NAME)

class PreferenceRepositoryImpl @Inject constructor(
    private val context: Context
) : PreferenceRepository {

    override suspend fun readUserData(): Flow<User> {
        return context.dataStore.data.map { preferences ->
            val uid = preferences[stringPreferencesKey(Constant.KEY_USER_UID)] ?: ""
            val user = preferences[stringPreferencesKey(Constant.KEY_USER_NAME)] ?: ""
            val info = preferences[stringPreferencesKey(Constant.KEY_USER_INFO)] ?: ""
            val email = preferences[stringPreferencesKey(Constant.KEY_USER_EMAIL)] ?: ""
            val image = preferences[stringPreferencesKey(Constant.KEY_USER_IMAGE)] ?: ""
            val provide = preferences[stringPreferencesKey(Constant.KEY_USER_PROVIDE)] ?: ""

            User(uid, user, info, email, image, provide)
        }
    }

    override suspend fun saveUserData(user: Map<String, String>) {
        context.dataStore.edit { preferences ->
            user.onEach { field ->
                preferences[stringPreferencesKey(field.key)] = field.value
            }
        }
    }

    override suspend fun clearUserData() {
        context.dataStore.edit { it.clear() }
    }
}