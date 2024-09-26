package epm.xnox.firebaseauth.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import epm.xnox.firebaseauth.data.repository.AuthRepositoryImpl
import epm.xnox.firebaseauth.data.repository.FirestoreRepositoryImpl
import epm.xnox.firebaseauth.data.repository.PreferenceRepositoryImpl
import epm.xnox.firebaseauth.data.repository.StorageRespositoryImpl
import epm.xnox.firebaseauth.domain.repository.AuthRepository
import epm.xnox.firebaseauth.domain.repository.FirestoreRepository
import epm.xnox.firebaseauth.domain.repository.PreferenceRepository
import epm.xnox.firebaseauth.domain.repository.StorageRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePreferenceRepository(@ApplicationContext context: Context): PreferenceRepository =
        PreferenceRepositoryImpl(context)

    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        database: FirebaseFirestore,
        storage: FirebaseStorage,
        preference: PreferenceRepository
    ): AuthRepository = AuthRepositoryImpl(auth, database, storage, preference)

    @Provides
    @Singleton
    fun provideStorageRepository(storage: FirebaseStorage): StorageRepository =
        StorageRespositoryImpl(storage)

    @Provides
    @Singleton
    fun provideFirestoreRepository(database: FirebaseFirestore): FirestoreRepository =
        FirestoreRepositoryImpl(database)
}