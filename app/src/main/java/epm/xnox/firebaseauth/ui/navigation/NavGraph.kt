package epm.xnox.firebaseauth.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import epm.xnox.firebaseauth.presentation.home.HomeScreen
import epm.xnox.firebaseauth.presentation.profile.ui.ProfileScreen
import epm.xnox.firebaseauth.presentation.profile.viewModel.ProfileViewModel
import epm.xnox.firebaseauth.presentation.signin.ui.SigninScreen
import epm.xnox.firebaseauth.presentation.signin.viewModel.SigninViewModel
import epm.xnox.firebaseauth.presentation.signup.ui.SignupScreen
import epm.xnox.firebaseauth.presentation.signup.viewModel.SignupViewModel

@Composable
fun NavGraph(startDestination: String) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = NavRoutes.ScreenHome.route) {
            HomeScreen(
                onNavigateToSignIn = {
                    navController.navigate(NavRoutes.ScreenSignIn.route)
                },
                onNavigateToSignUp = {
                    navController.navigate(NavRoutes.ScreenSignUp.route)
                }
            )
        }

        composable(route = NavRoutes.ScreenSignIn.route) {
            val signinViewModel = hiltViewModel<SigninViewModel>()
            SigninScreen(
                signinViewModel,
                onNavigateToRegister = {
                    navController.popBackStack()
                    navController.navigate(NavRoutes.ScreenSignUp.route)
                },
                onNavigateToBack = {
                    navController.popBackStack()
                },
                onNavigateToProfile = {
                    navController.popBackStack()
                    navController.navigate(NavRoutes.ScreenProfile.route) {
                        popUpTo(NavRoutes.ScreenHome.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = NavRoutes.ScreenSignUp.route) {
            val signupViewModel = hiltViewModel<SignupViewModel>()
            SignupScreen(
                signupViewModel,
                onNavigateToLogin = {
                    navController.popBackStack()
                    navController.navigate(NavRoutes.ScreenSignIn.route)
                },
                onNavigateToBack = {
                    navController.popBackStack()
                },
                onNavigateToProfile = {
                    navController.popBackStack()
                    navController.navigate(NavRoutes.ScreenProfile.route) {
                        popUpTo(NavRoutes.ScreenHome.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = NavRoutes.ScreenProfile.route) {
            val profileViewModel = hiltViewModel<ProfileViewModel>()
            ProfileScreen(
                profileViewModel,
                onNavigateToSignIn = {
                    navController.popBackStack()
                    navController.navigate(NavRoutes.ScreenSignIn.route)
                }
            )
        }
    }
}