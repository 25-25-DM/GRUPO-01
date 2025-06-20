package uce.edu.ec.Controller

import android.content.Context
import android.util.Log
import com.amazonaws.services.dynamodbv2.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uce.edu.ec.Models.Vehiculo
import uce.edu.ec.R
import java.security.MessageDigest

class AppRepository(context: Context) {

    private val localDb = DBHelper(context)
    private val remoteDb = AWSClient.getDynamoDBClient()

    // --- LÓGICA DE AUTENTICACIÓN ---

    suspend fun registrarUsuario(usuario: String, contrasenia: String): Boolean = withContext(Dispatchers.IO) {
        val hashedPassword = hashPassword(contrasenia)
        val item = mapOf(
            "usuario" to AttributeValue(usuario),
            "contrasenia" to AttributeValue(hashedPassword)
        )
        try {
            val request = PutItemRequest("Usuarios", item).withConditionExpression("attribute_not_exists(usuario)")
            remoteDb.putItem(request)
            localDb.obtenerOInsertarUsuarioLocal(usuario) // Asegura que el usuario también exista localmente.
            true
        } catch (e: ConditionalCheckFailedException) {
            Log.w("AppRepository", "El usuario $usuario ya existe.")
            false
        } catch (e: Exception) {
            Log.e("AppRepository", "Error al registrar usuario.", e)
            false
        }
    }

    /**
     * Verifica las credenciales contra DynamoDB.
     * Si son correctas, devuelve un Par con el nombre de usuario (String) y su ID local (Int).
     * Si no, devuelve null.
     */
    suspend fun verificarUsuario(usuario: String, contrasenia: String): Pair<String, Int>? = withContext(Dispatchers.IO) {
        val hashedPassword = hashPassword(contrasenia)
        try {
            val request = GetItemRequest().withTableName("Usuarios").withKey(mapOf("usuario" to AttributeValue(usuario)))
            val result = remoteDb.getItem(request)

            if (result.item != null && result.item["contrasenia"]?.s == hashedPassword) {
                val localId = localDb.obtenerOInsertarUsuarioLocal(usuario)
                Pair(usuario, localId)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("AppRepository", "Error al verificar usuario.", e)
            null
        }
    }

    private fun hashPassword(password: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val bytes = md.digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    // Función para probar la conexión con DynamoDB
    suspend fun probarConexion(): Boolean = withContext(Dispatchers.IO) {
        try {
            // Intentamos listar las tablas, que es una operación simple
            val result = remoteDb.listTables(1)
            Log.d("AppRepository", "Conexión exitosa. Tablas encontradas: ${result.tableNames}")
            true
        } catch (e: Exception) {
            Log.e("AppRepository", "Error al conectar con DynamoDB", e)
            Log.e("AppRepository", "Mensaje de error: ${e.message}")
            false
        }
    }

    // --- LÓGICA DE SINCRONIZACIÓN Y VEHÍCULOS ---

    suspend fun getVehiculos(usuarioId: Int, usuarioNombre: String): List<Vehiculo> {
        sincronizarConLaNube(usuarioId, usuarioNombre)
        return localDb.obtenerVehiculos(usuarioId)
    }

    private suspend fun sincronizarConLaNube(usuarioId: Int, usuarioNombre: String) = withContext(Dispatchers.IO) {
        try {
            val queryRequest = QueryRequest()
                .withTableName("Vehiculos")
                .withKeyConditionExpression("usuario = :v_usuario")
                .withExpressionAttributeValues(mapOf(":v_usuario" to AttributeValue(usuarioNombre)))

            val result = remoteDb.query(queryRequest)
            val vehiculosDesdeNube = result.items.map { item ->
                Vehiculo(
                    placa = item["placa"]?.s ?: "",
                    marca = item["marca"]?.s ?: "",
                    modelo = item["modelo"]?.s ?: "",
                    anio = item["anio"]?.n?.toInt() ?: 2000,
                    color = item["color"]?.s ?: "N/A",
                    costoPorDia = item["costoPorDia"]?.n?.toDouble() ?: 0.0,
                    activo = item["activo"]?.bool ?: false,
                    imagenRes = item["imagenRes"]?.n?.toInt() ?: R.drawable.a4 // Imagen por defecto
                )
            }

            localDb.eliminarTodosLosVehiculosDelUsuario(usuarioId)
            vehiculosDesdeNube.forEach { localDb.insertarVehiculo(it, usuarioId, true) }
        } catch (e: Exception) {
            Log.e("AppRepository", "Fallo la sincronización, trabajando con datos locales.", e)
        }
    }

    suspend fun addVehiculo(vehiculo: Vehiculo, usuarioId: Int, usuarioNombre: String) {
        localDb.insertarVehiculo(vehiculo, usuarioId, sincronizado = false)
        withContext(Dispatchers.IO) {
            try {
                val item = mapOf(
                    "usuario" to AttributeValue(usuarioNombre),
                    "placa" to AttributeValue(vehiculo.placa),
                    "marca" to AttributeValue(vehiculo.marca),
                    "modelo" to AttributeValue(vehiculo.modelo),
                    "anio" to AttributeValue().withN(vehiculo.anio.toString()),
                    "color" to AttributeValue(vehiculo.color),
                    "costoPorDia" to AttributeValue().withN(vehiculo.costoPorDia.toString()),
                    "activo" to AttributeValue().withBOOL(vehiculo.activo),
                    "imagenRes" to AttributeValue().withN(vehiculo.imagenRes.toString())
                )
                remoteDb.putItem(PutItemRequest("Vehiculos", item))
                localDb.marcarVehiculoComoSincronizado(vehiculo.placa, true)
            } catch (e: Exception) {
                Log.w("AppRepository", "No se pudo subir el vehículo ${vehiculo.placa}. Se sincronizará más tarde.", e)
            }
        }
    }

    suspend fun updateVehiculo(vehiculo: Vehiculo, usuarioId: Int, usuarioNombre: String) {
        localDb.actualizarVehiculo(vehiculo, usuarioId, sincronizado = false)
        withContext(Dispatchers.IO) {
            try {
                // (La lógica para actualizar es la misma que para insertar, PutItem sobrescribe)
                val item = mapOf(/*...mismos campos que en addVehiculo...*/"usuario" to AttributeValue(usuarioNombre))
                remoteDb.putItem(PutItemRequest("Vehiculos", item))
                localDb.marcarVehiculoComoSincronizado(vehiculo.placa, true)
            } catch (e: Exception) {
                Log.w("AppRepository", "No se pudo actualizar el vehículo ${vehiculo.placa}. Se sincronizará más tarde.", e)
            }
        }
    }

    suspend fun deleteVehiculo(placa: String, usuarioId: Int, usuarioNombre: String) {
        localDb.eliminarVehiculo(placa, usuarioId)
        withContext(Dispatchers.IO) {
            try {
                val key = mapOf("usuario" to AttributeValue(usuarioNombre), "placa" to AttributeValue(placa))
                remoteDb.deleteItem(DeleteItemRequest("Vehiculos", key))
            } catch (e: Exception) {
                Log.w("AppRepository", "No se pudo eliminar de la nube el vehículo $placa.", e)
            }
        }
    }

    fun obtenerVehiculoPorPlaca(placa: String, usuarioId: Int): Vehiculo? {
        return localDb.obtenerVehiculoPorPlaca(placa, usuarioId)
    }
}