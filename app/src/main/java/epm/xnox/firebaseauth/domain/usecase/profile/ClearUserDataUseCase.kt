package epm.xnox.firebaseauth.domain.usecase.profile

import epm.xnox.firebaseauth.domain.repository.PreferenceRepository
import javax.inject.Inject

class ClearUserDataUseCase @Inject constructor(private val repository: PreferenceRepository) {
    suspend operator fun invoke() = repository.clearUserData()
}