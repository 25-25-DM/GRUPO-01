package uce.edu.ec.View

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import uce.edu.ec.Models.Vehiculo
import uce.edu.ec.R
import uce.edu.ec.Controller.DBHelper

@Composable
fun HomeScreen(navController: NavController, dbHelper: DBHelper) {
    var listaVehiculos by remember { mutableStateOf(emptyList<Vehiculo>()) }

    val vehiculosPorDefecto = listOf(
        Vehiculo("ABC123", "Toyota", "Corolla", 2020, "Rojo", 45.0, true, R.drawable.a1),
        Vehiculo("XYZ789", "Chevrolet", "Spark", 2021, "Azul", 30.0, true, R.drawable.a2),
        Vehiculo("LMN456", "Kia", "Rio", 2019, "Negro", 35.0, false, R.drawable.a3)
    )

    LaunchedEffect(Unit) {
        val vehiculosEnDb = dbHelper.obtenerVehiculos()
        Log.d("HomeScreen", "Vehículos en BD antes de insertar: ${vehiculosEnDb.size}")
        if (vehiculosEnDb.isEmpty()) {
            vehiculosPorDefecto.forEach {
                val exito = dbHelper.insertarVehiculo(it)
                Log.d("HomeScreen", "Insertar vehiculo ${it.placa}: $exito")
            }
        }
        listaVehiculos = dbHelper.obtenerVehiculos()
        Log.d("HomeScreen", "Vehículos en BD después de insertar: ${listaVehiculos.size}")
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD7B8E8)) // Fondo gris suave
            .padding(16.dp)
    ) {
        item {
            Text(
                "Lista de Vehículos",
                style = MaterialTheme.typography.h5.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(listaVehiculos) { vehiculo ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = 6.dp,
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Image(
                        painter = painterResource(id = vehiculo.imagenRes),
                        contentDescription = "Imagen de ${vehiculo.marca} ${vehiculo.modelo}",
                        modifier = Modifier
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFFD7B8E8), // color arena
                                        Color.White        // blanco
                                    )
                                )
                                )
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Información del vehículo en filas ordenadas
                    VehicleInfoRow(label = "Placa", value = vehiculo.placa)
                    VehicleInfoRow(label = "Marca", value = vehiculo.marca)
                    VehicleInfoRow(label = "Modelo", value = vehiculo.modelo)
                    VehicleInfoRow(label = "Año", value = vehiculo.anio.toString())
                    VehicleInfoRow(label = "Color", value = vehiculo.color)
                    VehicleInfoRow(label = "Costo por día", value = "\$${vehiculo.costoPorDia}")
                    VehicleInfoRow(label = "Activo", value = if (vehiculo.activo) "Sí" else "No")

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            navController.navigate("editVehiculo/${vehiculo.placa}")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                    ) {
                        Text("Editar", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { navController.navigate("insertVehiculo") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF62D968))
            ) {
                Text("Agregar Vehículo", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("eliminarVehiculo") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFD32F2F)) // rojo fuerte
            ) {
                Text("Eliminar Vehículo", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedButton(
                onClick = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cerrar Sesión", color = MaterialTheme.colors.primary, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun VehicleInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontWeight = FontWeight.Medium, color = Color.Gray)
        Text(value, fontWeight = FontWeight.SemiBold)
    }
}
