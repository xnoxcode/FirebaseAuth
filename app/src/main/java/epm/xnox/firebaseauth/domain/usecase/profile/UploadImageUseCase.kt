package epm.xnox.firebaseauth.domain.usecase.profile

import epm.xnox.firebaseauth.domain.repository.StorageRepository
import javax.inject.Inject

class UploadImageUseCase @Inject constructor(private val repository: StorageRepository) {
    suspend operator fun invoke(uid: String, image: ByteArray) = repository.uploadImage(uid, image)
}