package epm.xnox.firebaseauth.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import epm.xnox.firebaseauth.core.Constant
import epm.xnox.firebaseauth.core.Response
import epm.xnox.firebaseauth.domain.repository.FirestoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreRepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore
): FirestoreRepository {

    override suspend fun updateUser(
        uid: String, user: Map<String, String>
    ): Flow<Response<Boolean>> = flow {
        emit(Response.Loading)

        try {
            database
                .collection(Constant.FIRESTORE_COLLECTION)
                .document(uid)
                .set(user)
                .await()
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: Constant.UNKNOW_ERROR))
        }
    }
}