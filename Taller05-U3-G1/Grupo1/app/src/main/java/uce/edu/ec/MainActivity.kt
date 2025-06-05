package uce.edu.ec

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uce.edu.ec.Controller.DBHelper
import uce.edu.ec.View.LoginScreen
import uce.edu.ec.View.RegisterScreen
import uce.edu.ec.ui.MarsPhotosApp
import uce.edu.ec.ui.screens.CountScreen
import uce.edu.ec.ui.screens.MarsUiState
import uce.edu.ec.ui.screens.MarsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dbHelper = DBHelper(this)

        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "login") {
                composable("login") { LoginScreen(navController) }
                composable("register") { RegisterScreen(navController) }
                composable("home") { MarsPhotosApp(navController)  }
                composable("count") {
                    val marsViewModel: MarsViewModel = viewModel()
                    val photos = (marsViewModel.marsUiState as? MarsUiState.Success)?.photos ?: emptyList()
                    CountScreen(photos = photos)
                }
            }
        }

    }
}
