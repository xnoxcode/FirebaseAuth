package epm.xnox.firebaseauth.domain.usecase.profile

import epm.xnox.firebaseauth.domain.mappper.toUser
import epm.xnox.firebaseauth.domain.model.User
import epm.xnox.firebaseauth.domain.repository.FirestoreRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(private val repository: FirestoreRepository) {
    suspend operator fun invoke(uid: String, user: User) = repository.updateUser(uid, user.toUser())
}