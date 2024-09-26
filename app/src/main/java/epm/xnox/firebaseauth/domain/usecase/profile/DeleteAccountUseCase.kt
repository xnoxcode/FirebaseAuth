package epm.xnox.firebaseauth.domain.usecase.profile

import epm.xnox.firebaseauth.domain.repository.AuthRepository
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(uid: String, email: String, password: String) =
        repository.deleteAccount(uid, email, password)
}