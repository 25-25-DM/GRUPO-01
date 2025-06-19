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
import androidx.compose.ui.Alignment
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
fun HomeScreen(navController: NavController, dbHelper: DBHelper, usuarioId: Int) {
    var listaVehiculos by remember { mutableStateOf(emptyList<Vehiculo>()) }
    var isLoading by remember { mutableStateOf(true) }

    // Generar vehículos por defecto con placas únicas por usuario
    val vehiculosPorDefecto = remember(usuarioId) {
        listOf(
            Vehiculo("ABC${usuarioId}01", "Toyota", "Corolla", 2020, "Rojo", 45.0, true, R.drawable.a1),
            Vehiculo("XYZ${usuarioId}02", "Chevrolet", "Spark", 2021, "Azul", 30.0, true, R.drawable.a2),
            Vehiculo("LMN${usuarioId}03", "Kia", "Rio", 2019, "Negro", 35.0, false, R.drawable.a3)
        )
    }

    // Usar remember para cargar los datos de forma segura
    val vehiculos = remember(usuarioId) {
        try {
            val vehiculosEnDb = dbHelper.obtenerVehiculos(usuarioId)
            Log.d("HomeScreen", "Vehículos en BD para usuario $usuarioId: ${vehiculosEnDb.size}")

            if (vehiculosEnDb.isEmpty()) {
                Log.d("HomeScreen", "La BD está vacía para el usuario $usuarioId, insertando datos por defecto.")
                vehiculosPorDefecto.forEach { vehiculo ->
                    val exito = dbHelper.insertarVehiculo(vehiculo, usuarioId)
                    Log.d("HomeScreen", "Insertar vehiculo ${vehiculo.placa} para usuario $usuarioId: $exito")
                }
                dbHelper.obtenerVehiculos(usuarioId)
            } else {
                vehiculosEnDb
            }
        } catch (e: Exception) {
            Log.e("HomeScreen", "Error al cargar vehículos: ${e.message}")
            emptyList()
        }
    }

    // Actualizar la lista cuando los vehículos cambien
    LaunchedEffect(vehiculos) {
        listaVehiculos = vehiculos
        isLoading = false
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD7B8E8))
            .padding(16.dp)
    ) {
        item {
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colors.primary
                    )
                }
            } else {
                Text(
                    "Lista de Vehículos",
                    style = MaterialTheme.typography.h5.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primary
                    )
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(listaVehiculos) { vehiculo ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = 6.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Image(
                        painter = painterResource(id = vehiculo.imagenRes),
                        contentDescription = "Imagen de ${vehiculo.marca} ${vehiculo.modelo}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color(0xFFD7B8E8), Color.White)
                                )
                            )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

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
                            navController.navigate("editVehiculo/${vehiculo.placa}/$usuarioId")
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
                onClick = { navController.navigate("insertVehiculo/$usuarioId") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF62D968))
            ) {
                Text(
                    "Agregar Vehículo",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("eliminarVehiculo/$usuarioId") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFD32F2F))
            ) {
                Text(
                    "Eliminar Vehículo",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedButton(
                onClick = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "Cerrar Sesión",
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Bold
                )
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