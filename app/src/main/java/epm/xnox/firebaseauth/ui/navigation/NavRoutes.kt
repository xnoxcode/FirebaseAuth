package epm.xnox.firebaseauth.ui.navigation

sealed class NavRoutes(val route: String) {

    data object ScreenHome : NavRoutes("home")

    data object ScreenSignIn: NavRoutes("signIn")

    data object ScreenSignUp : NavRoutes("signUp")

    data object ScreenProfile : NavRoutes("profile")
}