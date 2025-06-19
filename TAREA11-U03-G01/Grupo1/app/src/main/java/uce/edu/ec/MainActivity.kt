package uce.edu.ec

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import uce.edu.ec.Controller.DBHelper
import uce.edu.ec.Models.Vehiculo
import uce.edu.ec.View.EdicionVehiculoScreen
import uce.edu.ec.View.EliminarVehiculoScreen
import uce.edu.ec.View.HomeScreen
import uce.edu.ec.View.InsertVehiculoScreen
import uce.edu.ec.View.LoginScreen
import uce.edu.ec.View.RegisterScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Crear instancia de DBHelper una vez
        val dbHelper = DBHelper(this)

        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "login") {
                composable("login") { LoginScreen(navController, dbHelper) }
                composable("register") { RegisterScreen(navController, dbHelper) }
                composable("home/{usuarioId}") { backStackEntry ->
                    val usuarioId =
                        backStackEntry.arguments?.getString("usuarioId")?.toIntOrNull() ?: 0
                    HomeScreen(navController, dbHelper, usuarioId)
                }
                composable(
                    route = "insertVehiculo/{usuarioId}",
                    arguments = listOf(navArgument("usuarioId") {
                        type = NavType.IntType
                    })
                ) { backStackEntry ->
                    val usuarioId = backStackEntry.arguments?.getInt("usuarioId") ?: 0
                    InsertVehiculoScreen(navController, dbHelper, usuarioId)
                }

                composable(
                    route = "editVehiculo/{placa}/{usuarioId}",
                    arguments = listOf(
                        navArgument("placa") { type = NavType.StringType },
                        navArgument("usuarioId") { type = NavType.IntType }
                    )
                ) { backStackEntry ->
                    val placa = backStackEntry.arguments?.getString("placa") ?: ""
                    val usuarioId = backStackEntry.arguments?.getInt("usuarioId") ?: 0

                    val vehiculo: Vehiculo? = dbHelper.obtenerVehiculoPorPlaca(placa, usuarioId)

                    if (vehiculo != null) {
                        EdicionVehiculoScreen(navController, vehiculo, dbHelper, usuarioId)
                    } else {
                        navController.popBackStack()
                    }
                }

                composable(
                    route = "eliminarVehiculo/{usuarioId}",
                    arguments = listOf(navArgument("usuarioId") {
                        type = NavType.IntType
                    })
                ) { backStackEntry ->
                    val usuarioId = backStackEntry.arguments?.getInt("usuarioId") ?: 0
                    EliminarVehiculoScreen(navController, dbHelper, usuarioId)
                } 
            }
        }
    }
}
