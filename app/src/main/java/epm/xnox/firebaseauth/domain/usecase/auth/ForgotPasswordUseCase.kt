package epm.xnox.firebaseauth.domain.usecase.auth

import epm.xnox.firebaseauth.domain.repository.AuthRepository
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String) = repository.forgotPassword(email)
}