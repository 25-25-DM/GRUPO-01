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
                composable("login") { LoginScreen(navController) }
                composable("register") { RegisterScreen(navController) }
                composable("home") { HomeScreen(navController, dbHelper) }
                composable("insertVehiculo") { InsertVehiculoScreen(navController, dbHelper) }

                composable(
                    route = "editVehiculo/{placa}",
                    arguments = listOf(navArgument("placa") { type = NavType.StringType })
                ) { backStackEntry ->
                    val placa = backStackEntry.arguments?.getString("placa") ?: ""
                    val vehiculo: Vehiculo? = dbHelper.obtenerVehiculos().find { it.placa == placa }
                    if (vehiculo != null) {
                        EdicionVehiculoScreen(navController, vehiculo, dbHelper)
                    } else {
                        navController.popBackStack()
                    }
                }
                composable("eliminarVehiculo") { EliminarVehiculoScreen(navController, DBHelper(LocalContext.current)) }


            }
        }
    }
}
