package uce.edu.ec.View

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uce.edu.ec.Controller.DBHelper
import uce.edu.ec.Models.Vehiculo
import uce.edu.ec.R

@Composable
fun InsertVehiculoScreen(navController: NavController, dbHelper: DBHelper) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var placa by remember { mutableStateOf("") }
    var marca by remember { mutableStateOf("") }
    var modelo by remember { mutableStateOf("") }
    var anio by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var costoPorDia by remember { mutableStateOf("") }
    var activo by remember { mutableStateOf(true) }

    // Errores
    var placaError by remember { mutableStateOf<String?>(null) }
    var marcaError by remember { mutableStateOf<String?>(null) }
    var modeloError by remember { mutableStateOf<String?>(null) }
    var anioError by remember { mutableStateOf<String?>(null) }
    var colorError by remember { mutableStateOf<String?>(null) }
    var costoError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFD7B8E8), Color.White)
                )
            )
            .padding(horizontal = 24.dp, vertical = 90.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Insertar Vehículo",
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // PLACA
        OutlinedTextField(
            value = placa,
            onValueChange = {
                placa = it.uppercase()
                placaError = null
            },
            label = { Text("Placa (ABC123)") },
            isError = placaError != null,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        placaError?.let {
            Text(text = it, color = Color.Red, style = MaterialTheme.typography.caption)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // MARCA
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
        marcaError?.let {
            Text(text = it, color = Color.Red, style = MaterialTheme.typography.caption)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // MODELO
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
        modeloError?.let {
            Text(text = it, color = Color.Red, style = MaterialTheme.typography.caption)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // AÑO
        OutlinedTextField(
            value = anio,
            onValueChange = {
                anio = it.filter { c -> c.isDigit() }
                anioError = null
            },
            label = { Text("Año (2000-2050)") },
            isError = anioError != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        anioError?.let {
            Text(text = it, color = Color.Red, style = MaterialTheme.typography.caption)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // COLOR
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
        colorError?.let {
            Text(text = it, color = Color.Red, style = MaterialTheme.typography.caption)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // COSTO
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
        costoError?.let {
            Text(text = it, color = Color.Red, style = MaterialTheme.typography.caption)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = activo,
                onCheckedChange = { activo = it }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Activo", style = MaterialTheme.typography.body1)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // BOTÓN
        Button(
            onClick = {
                var valido = true

                // Validación PLACA
                val regexPlaca = Regex("^[A-Z]{3}\\d{3}$")
                if (!regexPlaca.matches(placa)) {
                    placaError = "Placa inválida. Ej: ABC123"
                    valido = false
                }

                // Validación MARCA
                val regexLetras = Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")
                if (marca.isBlank() || !regexLetras.matches(marca)) {
                    marcaError = "Marca inválida. Solo letras y espacios."
                    valido = false
                }

                // Validación MODELO
                if (modelo.isBlank() || !regexLetras.matches(modelo)) {
                    modeloError = "Modelo inválido. Solo letras y espacios."
                    valido = false
                }

                // Validación AÑO
                val anioInt = anio.toIntOrNull()
                if (anioInt == null || anioInt < 2000 || anioInt > 2050) {
                    anioError = "Año inválido. Entre 2000 y 2050."
                    valido = false
                }

                // Validación COLOR
                if (color.isBlank() || !regexLetras.matches(color)) {
                    colorError = "Color inválido. Solo letras."
                    valido = false
                }

                // Validación COSTO
                val costo = costoPorDia.toDoubleOrNull()
                if (costo == null || costo <= 0) {
                    costoError = "Costo inválido. Solo números positivos."
                    valido = false
                }

                if (valido) {
                    val vehiculo = Vehiculo(
                        placa = placa,
                        marca = marca,
                        modelo = modelo,
                        anio = anioInt!!,
                        color = color,
                        costoPorDia = costo!!,
                        activo = activo,
                        imagenRes = R.drawable.a4
                    )
                    val exito = dbHelper.insertarVehiculo(vehiculo)
                    if (exito) {
                        Toast.makeText(context, "Vehículo insertado", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context, "Error al insertar vehículo", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Guardar Vehículo", fontWeight = FontWeight.Bold)
        }
    }
}
