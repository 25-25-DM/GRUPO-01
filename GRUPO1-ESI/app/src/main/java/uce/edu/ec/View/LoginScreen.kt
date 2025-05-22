package uce.edu.ec.View

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uce.edu.ec.R
import uce.edu.ec.Controller.DBHelper

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val db = remember { DBHelper(context) }

    var usuario by remember { mutableStateOf("") }
    var contrasenia by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var hashValue by remember { mutableStateOf("") }

    val morado = Color(0xFF5635DC)
    val rosa = Color(0xFFDA00FF)

    var errorUsuario by remember { mutableStateOf<String?>(null) }
    var errorContrasenia by remember { mutableStateOf<String?>(null) }

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
        /* else if (contrasenia.length < 6) {
            errorContrasenia = "Mínimo 6 caracteres"
        } else if (!contrasenia.any { it.isDigit() } || !contrasenia.any { it.isLetter() }) {
            errorContrasenia = "Debe contener letras y números"
        } else if (contrasenia.first().isLetterOrDigit().not()) {
            errorContrasenia = "No puede empezar con símbolo"
        } else if (contrasenia.contains(" ")) {
            errorContrasenia = "No puede contener espacios"
        } */

        return errorUsuario == null && errorContrasenia == null
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { 
                showDialog = false
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            },
            title = { Text("Información de Inicio de Sesión") },
            text = {
                Column {
                    Text("Bienvenido: $usuario")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Contraseña: $contrasenia")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Hash: $hashValue")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = morado)
                ) {
                    Text("Continuar", color = Color.White)
                }
            },
            backgroundColor = Color.White,
            contentColor = Color.Black
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = "Fondo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        // Capa de oscurecimiento encima del fondo
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.White.copy(alpha = 0.8f)) // Ajusta el alpha según qué tan oscuro quieras
        )

        // Contenido encima
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Image(
                painter = painterResource(id = R.drawable.a5),
                contentDescription = "Logo Autos",
                modifier = Modifier.size(350.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it },
                label = { Text("Usuario") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = rosa,
                    unfocusedBorderColor = morado,
                    focusedLabelColor = rosa
                ),
                shape = RoundedCornerShape(12.dp),
                isError = errorUsuario != null
            )

            if (errorUsuario != null) {
                Text(
                    text = errorUsuario!!,
                    color = Color.Red,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 8.dp, top = 4.dp)
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
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = rosa,
                    unfocusedBorderColor = morado,
                    focusedLabelColor = rosa
                ),
                shape = RoundedCornerShape(12.dp),
                isError = errorContrasenia != null,
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

            if (errorContrasenia != null) {
                Text(
                    text = errorContrasenia!!,
                    color = Color.Red,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (validarCredenciales(usuario, contrasenia)) {
                        if (db.verificarUsuario(usuario, contrasenia)) {
                            hashValue = db.obtenerHashContrasenia(usuario) ?: "No encontrado"
                            showDialog = true
                        } else {
                            Toast.makeText(context, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = morado)
            ) {
                Text("Iniciar Sesión", style = MaterialTheme.typography.button, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "¿No tienes cuenta? Regístrate aquí",
                color = morado,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.clickable {
                    navController.navigate("register")
                }
            )
        }
    }
}
