package epm.xnox.firebaseauth.data.repository

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import epm.xnox.firebaseauth.core.Constant
import epm.xnox.firebaseauth.core.Response
import epm.xnox.firebaseauth.data.mapper.toUser
import epm.xnox.firebaseauth.domain.repository.AuthRepository
import epm.xnox.firebaseauth.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val preference: PreferenceRepository
) : AuthRepository {

    override suspend fun signUpWithEmailAndPassword(
        user: String,
        email: String,
        password: String
    ): Flow<Response<Boolean>> = flow {
        emit(Response.Loading)
        try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val userResult = authResult.user

            if (userResult != null) {
                val uid = userResult.uid
                val userResponse =
                    userResult.toUser(Constant.PROVIDE_EMAIL, userName = user, userEmail = email)
                preference.saveUserData(userResponse)
                addUserToFirestore(uid, userResponse)
                emit(Response.Success(true))
            } else {
                emit(Response.Error(Constant.AUTH_USER_RESULT_NULL))
            }
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: Constant.UNKNOW_ERROR))
        }
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Response<Boolean>> = flow {
        emit(Response.Loading)

        try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val userResult = authResult.user

            if (userResult != null) {
                val uid = userResult.uid
                val userResponse = readUserFromFirestore(uid)
                preference.saveUserData(userResponse)
                emit(Response.Success(true))
            } else {
                emit(Response.Error(Constant.AUTH_USER_RESULT_NULL))
            }
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: Constant.UNKNOW_ERROR))
        }
    }

    override suspend fun signInWithCredential(idToken: String): Flow<Response<Boolean>> = flow {
        emit(Response.Loading)

        try {
            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = auth.signInWithCredential(firebaseCredential).await()
            val isNewUser = authResult.additionalUserInfo?.isNewUser == true
            val userResult = authResult.user

            if (userResult != null) {
                val uid = userResult.uid
                if (isNewUser) {
                    val userResponse = userResult.toUser(Constant.PROVIDE_GOOGLE)
                    preference.saveUserData(userResponse)
                    addUserToFirestore(uid, userResponse)
                } else {
                    val userResponse = readUserFromFirestore(uid)
                    preference.saveUserData(userResponse)
                }
                emit(Response.Success(true))
            } else {
                emit(Response.Error(Constant.AUTH_USER_RESULT_NULL))
            }
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: Constant.UNKNOW_ERROR))
        }
    }

    override suspend fun deleteAccount(
        uid: String,
        email: String,
        password: String
    ): Flow<Response<Boolean>> = flow {

        emit(Response.Loading)
        try {
            val user = auth.currentUser

            if (user != null) {
                val credential = EmailAuthProvider.getCredential(email, password)
                user.reauthenticate(credential).await()
                user.delete().await()

                database
                    .collection(Constant.FIRESTORE_COLLECTION)
                    .document(uid)
                    .delete()
                    .await()

                runCatching {
                    storage.reference
                        .child("${Constant.STORAGE_CHILD_IMAGES}/$uid.jpg")
                        .delete()
                        .await()
                }
                emit(Response.Success(true))
            } else {
                emit(Response.Error(Constant.AUTH_USER_RESULT_NULL))
            }
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: Constant.UNKNOW_ERROR))
        }
    }

    override suspend fun logoutUser() {
        preference.clearUserData()
        auth.signOut()
    }

    override suspend fun forgotPassword(email: String): Flow<Response<Boolean>> = flow {
        emit(Response.Loading)
        try {
            auth.sendPasswordResetEmail(email).await()
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: Constant.UNKNOW_ERROR))
        }
    }

    private suspend fun readUserFromFirestore(uid: String): Map<String, String> {
        val document = database
            .collection(Constant.FIRESTORE_COLLECTION)
            .document(uid)
            .get()
            .await()
        return document.toUser()
    }

    private suspend fun addUserToFirestore(uid: String, user: Map<String, String>) {
        database
            .collection(Constant.FIRESTORE_COLLECTION)
            .document(uid)
            .set(user)
            .await()
    }
}