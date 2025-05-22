package uce.edu.ec.View

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uce.edu.ec.Controller.DBHelper
import uce.edu.ec.Models.Vehiculo

@Composable
fun EdicionVehiculoScreen(navController: NavController, vehiculoEditar: Vehiculo, dbHelper: DBHelper) {
    val context = LocalContext.current

    var marca by remember { mutableStateOf(vehiculoEditar.marca) }
    var modelo by remember { mutableStateOf(vehiculoEditar.modelo) }
    var anio by remember { mutableStateOf(vehiculoEditar.anio.toString()) }
    var color by remember { mutableStateOf(vehiculoEditar.color) }
    var costoPorDia by remember { mutableStateOf(vehiculoEditar.costoPorDia.toString()) }
    var activo by remember { mutableStateOf(vehiculoEditar.activo) }

    // Estados de error
    var marcaError by remember { mutableStateOf<String?>(null) }
    var modeloError by remember { mutableStateOf<String?>(null) }
    var anioError by remember { mutableStateOf<String?>(null) }
    var colorError by remember { mutableStateOf<String?>(null) }
    var costoError by remember { mutableStateOf<String?>(null) }

    val rosa = Color(0xFFD7B8E8)
    val scrollState = rememberScrollState()
    val backgroundGradient = Brush.verticalGradient(colors = listOf(rosa, Color.White))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .padding(horizontal = 24.dp, vertical = 90.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Editar Vehículo - Placa: ${vehiculoEditar.placa}",
            style = MaterialTheme.typography.h6.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Marca
        OutlinedTextField(
            value = marca,
            onValueChange = {
                marca = it
                marcaError = null
            },
            label = { Text("Marca") },
            isError = marcaError != null,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        marcaError?.let { Text(it, color = Color.Red, style = MaterialTheme.typography.caption) }

        Spacer(modifier = Modifier.height(12.dp))

        // Modelo
        OutlinedTextField(
            value = modelo,
            onValueChange = {
                modelo = it
                modeloError = null
            },
            label = { Text("Modelo") },
            isError = modeloError != null,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        modeloError?.let { Text(it, color = Color.Red, style = MaterialTheme.typography.caption) }

        Spacer(modifier = Modifier.height(12.dp))

        // Año
        OutlinedTextField(
            value = anio,
            onValueChange = {
                anio = it.filter { c -> c.isDigit() }
                anioError = null
            },
            label = { Text("Año") },
            isError = anioError != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        anioError?.let { Text(it, color = Color.Red, style = MaterialTheme.typography.caption) }

        Spacer(modifier = Modifier.height(12.dp))

        // Color
        OutlinedTextField(
            value = color,
            onValueChange = {
                color = it
                colorError = null
            },
            label = { Text("Color") },
            isError = colorError != null,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        colorError?.let { Text(it, color = Color.Red, style = MaterialTheme.typography.caption) }

        Spacer(modifier = Modifier.height(12.dp))

        // Costo por día
        OutlinedTextField(
            value = costoPorDia,
            onValueChange = {
                costoPorDia = it.filter { c -> c.isDigit() || c == '.' }
                costoError = null
            },
            label = { Text("Costo por día") },
            isError = costoError != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        costoError?.let { Text(it, color = Color.Red, style = MaterialTheme.typography.caption) }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = activo,
                onCheckedChange = { activo = it },
                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colors.primary)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Activo")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                val regexLetrasEspacios = Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")

                when {
                    marca.isBlank() || !regexLetrasEspacios.matches(marca) -> {
                        marcaError = "Marca inválida. Solo letras y espacios."
                    }

                    modelo.isBlank() || !regexLetrasEspacios.matches(modelo) -> {
                        modeloError = "Modelo inválido. Solo letras y espacios."
                    }

                    anio.isBlank() || anio.length != 4 || anio.toIntOrNull() == null || anio.toInt() < 1900 || anio.toInt() > 2025 -> {
                        anioError = "Año inválido. Debe tener 4 dígitos entre 1900 y 2025."
                    }

                    color.isBlank() || !regexLetrasEspacios.matches(color) -> {
                        colorError = "Color inválido. Solo letras y espacios."
                    }

                    costoPorDia.isBlank() || costoPorDia.toDoubleOrNull() == null || costoPorDia.toDouble() <= 0 -> {
                        costoError = "Costo inválido. Debe ser mayor que 0."
                    }

                    else -> {
                        val vehiculoActualizado = Vehiculo(
                            placa = vehiculoEditar.placa,
                            marca = marca,
                            modelo = modelo,
                            anio = anio.toInt(),
                            color = color,
                            costoPorDia = costoPorDia.toDouble(),
                            activo = activo,
                            imagenRes = vehiculoEditar.imagenRes
                        )

                        val exito = dbHelper.actualizarVehiculo(vehiculoActualizado)

                        if (exito) {
                            Toast.makeText(context, "Vehículo actualizado correctamente", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        } else {
                            Toast.makeText(context, "Error al actualizar el vehículo", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Guardar Cambios")
        }
    }
}
