package epm.xnox.firebaseauth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import epm.xnox.firebaseauth.ui.navigation.NavGraph
import epm.xnox.firebaseauth.ui.navigation.NavRoutes
import epm.xnox.firebaseauth.ui.theme.FirebaseAuthTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FirebaseAuthTheme {
                Surface {
                    val route = if (auth.currentUser != null)
                        NavRoutes.ScreenProfile.route
                    else
                        NavRoutes.ScreenHome.route
                    NavGraph(startDestination = route)
                }
            }
        }
    }
}
