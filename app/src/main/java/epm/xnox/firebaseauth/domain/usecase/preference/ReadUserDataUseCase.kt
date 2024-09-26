package epm.xnox.firebaseauth.domain.usecase.preference

import epm.xnox.firebaseauth.domain.repository.PreferenceRepository
import javax.inject.Inject

class ReadUserDataUseCase @Inject constructor(private val repository: PreferenceRepository) {
    suspend operator fun invoke() = repository.readUserData()
}