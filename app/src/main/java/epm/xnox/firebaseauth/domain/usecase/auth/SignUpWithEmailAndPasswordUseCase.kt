package epm.xnox.firebaseauth.domain.usecase.auth

import epm.xnox.firebaseauth.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpWithEmailAndPasswordUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(user: String, email: String, password: String) =
        repository.signUpWithEmailAndPassword(user, email, password)
}