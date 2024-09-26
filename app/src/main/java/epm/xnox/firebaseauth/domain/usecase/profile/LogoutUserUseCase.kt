package epm.xnox.firebaseauth.domain.usecase.profile

import epm.xnox.firebaseauth.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUserUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke() = repository.logoutUser()
}