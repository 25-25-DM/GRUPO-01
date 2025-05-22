package uce.edu.ec.View

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uce.edu.ec.Controller.DBHelper

@Composable
fun RegisterScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { DBHelper(context) }

    var usuario by remember { mutableStateOf("") }
    var contrasenia by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var errorUsuario by remember { mutableStateOf<String?>(null) }
    var errorContrasenia by remember { mutableStateOf<String?>(null) }

    val amarillo = Color(0xFFD7B8E8)
    val morado = Color(0xFF5635DC)

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(amarillo, Color.White)
    )

    fun validarCredenciales(usuario: String, contrasenia: String): Boolean {
        errorUsuario = null
        errorContrasenia = null

        // Validación de usuario
        if (usuario.isBlank()) {
            errorUsuario = "El usuario no puede estar vacío"
        } else if (usuario.startsWith("-")) {
            errorUsuario = "No puede comenzar con '-'"
        } else if (!usuario.matches(Regex("^[a-zA-Z0-9_]{4,16}$"))) {
            errorUsuario = "Solo letras, números o '_' (4-16 caracteres)"
        } else if (usuario.all { it.isDigit() } || usuario.all { it.isLetter() }) {
            errorUsuario = "Debe contener letras y números"
        }

        // Validación de contraseña
        if (contrasenia.isBlank()) {
            errorContrasenia = "La contraseña no puede estar vacía"
        }
        /* else {
            // Validar al menos 1 mayúscula
            if (!contrasenia.any { it.isUpperCase() }) {
                errorContrasenia = "La contraseña debe contener al menos 1 mayúscula"
            }
            // Validar máximo 3 minúsculas
            else if (contrasenia.count { it.isLowerCase() } > 3) {
                errorContrasenia = "La contraseña debe contener máximo 3 minúsculas"
            }
            // Validar exactamente 3 números
            else if (contrasenia.count { it.isDigit() } != 3) {
                errorContrasenia = "La contraseña debe contener exactamente 3 números"
            }
            // Validar que termine con al menos uno de los caracteres especiales
            else if (!contrasenia.matches(Regex(".*[#@_!]$"))) {
                errorContrasenia = "La contraseña debe terminar con uno de estos caracteres: #, @, _, !"
            }
        } */

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
            "Registro de Usuario",
            style = MaterialTheme.typography.h6,
            color = morado
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it },
            label = { Text("Nombre de usuario") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            isError = errorUsuario != null,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = morado,
                unfocusedBorderColor = morado.copy(alpha = 0.5f),
                focusedLabelColor = morado,
                errorBorderColor = Color.Red
            ),
            shape = RoundedCornerShape(12.dp)
        )

        errorUsuario?.let {
            Text(
                text = it,
                color = Color.Red,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.align(Alignment.Start).padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = contrasenia,
            onValueChange = { contrasenia = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            isError = errorContrasenia != null,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = morado,
                unfocusedBorderColor = morado.copy(alpha = 0.5f),
                focusedLabelColor = morado,
                errorBorderColor = Color.Red
            ),
            shape = RoundedCornerShape(12.dp),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                        tint = morado
                    )
                }
            }
        )

        errorContrasenia?.let {
            Text(
                text = it,
                color = Color.Red,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.align(Alignment.Start).padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (validarCredenciales(usuario, contrasenia)) {
                    val registrado = db.registrarUsuario(usuario, contrasenia)
                    if (registrado) {
                        Toast.makeText(context, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show()
                        navController.navigate("login") {
                            popUpTo("register") { inclusive = true }
                        }
                    } else {
                        Toast.makeText(context, "Error: el usuario ya existe", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = morado)
        ) {
            Text("Registrarse", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "¿Ya tienes cuenta? Inicia sesión",
            color = morado,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.clickable {
                navController.navigate("login")
            }
        )
    }
}
