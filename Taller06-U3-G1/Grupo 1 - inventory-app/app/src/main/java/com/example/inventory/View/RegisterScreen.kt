package com.example.inventory.View

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.inventory.data.DBHelper
import com.example.inventory.R
import com.example.inventory.ui.navigation.NavigationDestination

object RegisterDestination : NavigationDestination {
    override val route = "register"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { DBHelper(context) }

    var usuario by remember { mutableStateOf("") }
    var contrasenia by remember { mutableStateOf("") }

    val morado = Color(0xFF5635DC)
    val amarillo = Color(0xFFD7B8E8)
    val rosa = Color(0xFFDA00FF)

    var errorUsuario by remember { mutableStateOf<String?>(null) }
    var errorContrasenia by remember { mutableStateOf<String?>(null) }

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(amarillo, Color.White)
    )

    fun validarCredenciales(usuario: String, contrasenia: String): Boolean {
        errorUsuario = null
        errorContrasenia = null

        if (usuario.isBlank()) {
            errorUsuario = "El usuario no puede estar vacío"
        } else if (usuario.startsWith("-")) {
            errorUsuario = "No puede comenzar con '-'"
        } else if (!usuario.matches(Regex("^[a-zA-Z0-9_]{4,16}$"))) {
            errorUsuario = "Solo letras, números o '_' (4-16 caracteres)"
        } else if (usuario.all { it.isDigit() } || usuario.all { it.isLetter() }) {
            errorUsuario = "Debe contener letras y números"
        }

        if (contrasenia.isBlank()) {
            errorContrasenia = "La contraseña no puede estar vacía"
        } else if (contrasenia.length < 6) {
            errorContrasenia = "Mínimo 6 caracteres"
        } else if (!contrasenia.any { it.isDigit() } || !contrasenia.any { it.isLetter() }) {
            errorContrasenia = "Debe contener letras y números"
        } else if (contrasenia.first().isLetterOrDigit().not()) {
            errorContrasenia = "No puede empezar con símbolo"
        } else if (contrasenia.contains(" ")) {
            errorContrasenia = "No puede contener espacios"
        }

        return errorUsuario == null && errorContrasenia == null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Registro de Usuario",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = morado
            ),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it },
            label = { Text("Nombre de Usuario",
                color = Color.Black.copy(alpha = 0.5f),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            ) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = rosa,
                unfocusedBorderColor = morado,
                focusedLabelColor = rosa,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = Color.Black,
                errorBorderColor = Color.Red,
                errorTextColor = Color.Black,
                errorLabelColor = Color.Red,
                errorCursorColor = Color.Black
            ),
            shape = RoundedCornerShape(12.dp),
            isError = errorUsuario != null
        )

        if (errorUsuario != null) {
            Text(
                text = errorUsuario!!,
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Red),
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 4.dp, bottom = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = contrasenia,
            onValueChange = { contrasenia = it },
            label = { Text("Contraseña",
                color = Color.Black.copy(alpha = 0.5f),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            ) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = rosa,
                unfocusedBorderColor = morado,
                focusedLabelColor = rosa,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = Color.Black,
                errorBorderColor = Color.Red,
                errorTextColor = Color.Black,
                errorLabelColor = Color.Red,
                errorCursorColor = Color.Black
            ),
            shape = RoundedCornerShape(12.dp),
            isError = errorContrasenia != null
        )

        if (errorContrasenia != null) {
            Text(
                text = errorContrasenia!!,
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Red),
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 4.dp, bottom = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (validarCredenciales(usuario, contrasenia)) {
                    if (db.registrarUsuario(usuario, contrasenia)) {
                        Toast.makeText(context, "¡Registro exitoso!", Toast.LENGTH_LONG).show()
                        navController.navigate("login") {
                            popUpTo("register") { inclusive = true }
                        }
                    } else {
                        Toast.makeText(context, "El usuario ya existe", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = morado)
        ) {
            Text(
                "Registrarse", 
                style = MaterialTheme.typography.labelLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        TextButton(
            onClick = { 
                navController.navigate("login") {
                    popUpTo("register") { inclusive = true }
                }
            },
            modifier = Modifier.padding(top = 8.dp),
            colors = ButtonDefaults.textButtonColors(contentColor = morado)
        ) {
            Text("¿Ya tienes cuenta? Inicia Sesión")
        }
    }
}
