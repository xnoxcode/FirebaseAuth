package epm.xnox.firebaseauth.domain.usecase.auth

import epm.xnox.firebaseauth.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithCredentialUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(idToken: String) =
        repository.signInWithCredential(idToken)
}