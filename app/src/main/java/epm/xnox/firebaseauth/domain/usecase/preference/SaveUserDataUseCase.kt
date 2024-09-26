package epm.xnox.firebaseauth.domain.usecase.preference

import epm.xnox.firebaseauth.domain.mappper.toUser
import epm.xnox.firebaseauth.domain.model.User
import epm.xnox.firebaseauth.domain.repository.PreferenceRepository
import javax.inject.Inject

class SaveUserDataUseCase @Inject constructor(private val repository: PreferenceRepository) {
    suspend operator fun invoke(user: User) = repository.saveUserData(user.toUser())
}