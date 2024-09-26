package epm.xnox.firebaseauth.core

object Constant {

    const val UNEXPECTED_CREDENTIAL = "Unexpected credential"
    const val FIRESTORE_COLLECTION = "users"
    const val STORAGE_CHILD_IMAGES = "images"
    const val PREFERENCE_NAME = "preferences"
    const val KEY_USER_UID = "uid"
    const val KEY_USER_NAME = "user"
    const val KEY_USER_INFO = "info"
    const val KEY_USER_EMAIL = "email"
    const val KEY_USER_IMAGE = "url"
    const val KEY_USER_PROVIDE = "provide"
    const val FIRESTORE_CHILD_INFO_DEFAULT = "Hola!"
    const val PROVIDE_EMAIL = "email"
    const val PROVIDE_GOOGLE = "google"

    //Exception
    const val UNKNOW_ERROR = "Unknow error"
    const val AUTH_USER_RESULT_NULL = "No se pudo cargar el usuario, inténtalo de nuevo"
    const val STORAGE_UPLOAD_ERROR = "Error al subir la imagen"
    const val STORAGE_DOWNLOAD_URL_ERROR = "Error al obtener la url"
}