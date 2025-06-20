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
import uce.edu.ec.Controller.AppRepository
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

        // Crear instancias de las dependencias
        val repository = AppRepository(this)

        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "login") {
                composable("login") { LoginScreen(navController, repository) }
                composable("register") { RegisterScreen(navController, repository) }
                
                composable(
                    route = "home/{usuarioId}/{usuarioNombre}",
                    arguments = listOf(
                        navArgument("usuarioId") { type = NavType.IntType },
                        navArgument("usuarioNombre") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val usuarioId = backStackEntry.arguments?.getInt("usuarioId") ?: 0
                    val usuarioNombre = backStackEntry.arguments?.getString("usuarioNombre") ?: ""
                    HomeScreen(navController, repository, usuarioId, usuarioNombre)
                }

                composable(
                    route = "insertVehiculo/{usuarioId}/{usuarioNombre}",
                    arguments = listOf(
                        navArgument("usuarioId") { type = NavType.IntType },
                        navArgument("usuarioNombre") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val usuarioId = backStackEntry.arguments?.getInt("usuarioId") ?: 0
                    val usuarioNombre = backStackEntry.arguments?.getString("usuarioNombre") ?: ""
                    InsertVehiculoScreen(navController, repository, usuarioId, usuarioNombre)
                }

                composable(
                    route = "editVehiculo/{placa}/{usuarioId}/{usuarioNombre}",
                    arguments = listOf(
                        navArgument("placa") { type = NavType.StringType },
                        navArgument("usuarioId") { type = NavType.IntType },
                        navArgument("usuarioNombre") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val placa = backStackEntry.arguments?.getString("placa") ?: ""
                    val usuarioId = backStackEntry.arguments?.getInt("usuarioId") ?: 0
                    val usuarioNombre = backStackEntry.arguments?.getString("usuarioNombre") ?: ""

                    val vehiculo = repository.obtenerVehiculoPorPlaca(placa, usuarioId)
                    if (vehiculo != null) {
                        EdicionVehiculoScreen(navController, vehiculo, repository, usuarioId, usuarioNombre)
                    } else {
                        navController.popBackStack()
                    }
                }

                composable(
                    route = "eliminarVehiculo/{usuarioId}/{usuarioNombre}",
                    arguments = listOf(
                        navArgument("usuarioId") { type = NavType.IntType },
                        navArgument("usuarioNombre") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val usuarioId = backStackEntry.arguments?.getInt("usuarioId") ?: 0
                    val usuarioNombre = backStackEntry.arguments?.getString("usuarioNombre") ?: ""
                    EliminarVehiculoScreen(navController, repository, usuarioId, usuarioNombre)
                }
            }
        }
    }
}
