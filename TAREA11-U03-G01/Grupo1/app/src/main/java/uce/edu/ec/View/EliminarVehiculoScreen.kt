package uce.edu.ec.View

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uce.edu.ec.Controller.AppRepository
import kotlinx.coroutines.launch

@Composable
fun EliminarVehiculoScreen(navController: NavController, repository: AppRepository, usuarioId: Int, usuarioNombre: String) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var placa by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFD7B8E8),
                        Color(0xFFFFFFFF)
                    )
                )
            )
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Eliminar Vehículo",
                style = MaterialTheme.typography.h6.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = placa,
                onValueChange = { placa = it.uppercase() },
                label = { Text("Placa del vehículo a eliminar") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colors.primary)
            } else {
                Button(
                    onClick = {
                        if (placa.isNotBlank()) {
                            isLoading = true
                            scope.launch {
                                try {
                                    repository.deleteVehiculo(placa, usuarioId, usuarioNombre)
                                    Toast.makeText(context, "Vehículo eliminado exitosamente", Toast.LENGTH_SHORT).show()
                                    navController.popBackStack()
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Error al eliminar el vehículo: ${e.message}", Toast.LENGTH_LONG).show()
                                } finally {
                                    isLoading = false
                                }
                            }
                        } else {
                            Toast.makeText(context, "Ingrese la placa", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)
                ) {
                    Text(
                        text = "Eliminar Vehículo",
                        style = MaterialTheme.typography.button.copy(
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}
