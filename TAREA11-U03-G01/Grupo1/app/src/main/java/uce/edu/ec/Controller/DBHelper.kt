package uce.edu.ec.Controller

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import uce.edu.ec.Models.Vehiculo
import java.security.MessageDigest

class DBHelper(context: Context) : SQLiteOpenHelper(context, "usuarios.db", null, 3) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            """
            CREATE TABLE usuarios(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                usuario TEXT,
                contrasenia TEXT
            )
        """.trimIndent()
        )
        // Crear tabla vehiculos
        db?.execSQL(
            """
            CREATE TABLE vehiculos(
                placa TEXT PRIMARY KEY,
                marca TEXT,
                modelo TEXT,
                anio INTEGER,
                color TEXT,
                costoPorDia REAL,
                activo INTEGER,
                imagenRes INTEGER,
                usuario_id INTEGER,  -- Nueva columna para la relación
                FOREIGN KEY(usuario_id) REFERENCES usuarios(id) -- Definición de la relación
)
        """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS usuarios")
        db?.execSQL("DROP TABLE IF EXISTS vehiculos")
        onCreate(db)
    }

    // Función para convertir la contraseña en hash SHA-256
    private fun hashPassword(password: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val bytes = md.digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun registrarUsuario(usuario: String, contrasenia: String): Boolean {
        val db = writableDatabase
        val hashedPassword = hashPassword(contrasenia)  // Aquí hasheamos la contraseña
        val values = ContentValues().apply {
            put("usuario", usuario)
            put("contrasenia", hashedPassword)
        }
        return db.insert("usuarios", null, values) != -1L
    }

    fun verificarUsuario(usuario: String, contrasenia: String): Int? { // Devuelve Int? (un entero que puede ser nulo)
        val db = readableDatabase
        val hashedPassword = hashPassword(contrasenia)
        val cursor = db.rawQuery(
            "SELECT id FROM usuarios WHERE usuario=? AND contrasenia=?", // Pide solo el ID
            arrayOf(usuario, hashedPassword)
        )
        val userId: Int? = if (cursor.moveToFirst()) {
            cursor.getInt(cursor.getColumnIndexOrThrow("id"))
        } else {
            null // Si no lo encuentra, devuelve nulo
        }
        cursor.close()
        return userId
    }
// Funciones para vehículos:

    fun insertarVehiculo(vehiculo: Vehiculo, usuarioId: Int): Boolean {

        val db = writableDatabase
        val values = ContentValues().apply {
            put("placa", vehiculo.placa)
            put("marca", vehiculo.marca)
            put("modelo", vehiculo.modelo)
            put("anio", vehiculo.anio)
            put("color", vehiculo.color)
            put("costoPorDia", vehiculo.costoPorDia)
            put("activo", if (vehiculo.activo) 1 else 0)
            put("imagenRes", vehiculo.imagenRes)
            put("usuario_id", usuarioId)
        }
        return db.insert("vehiculos", null, values) != -1L
    }

    // La función ahora requiere el usuarioId para ser segura
    fun actualizarVehiculo(vehiculo: Vehiculo): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("marca", vehiculo.marca)
            put("modelo", vehiculo.modelo)
            put("anio", vehiculo.anio)
            put("color", vehiculo.color)
            put("costoPorDia", vehiculo.costoPorDia)
            put("activo", if (vehiculo.activo) 1 else 0)
            put("imagenRes", vehiculo.imagenRes)
        }
        val updated = db.update("vehiculos", values, "placa=?", arrayOf(vehiculo.placa))
        return updated > 0
    }

    fun eliminarVehiculo(placa: String, usuarioId: Int): Boolean {
        val db = writableDatabase
        // 2. La condición WHERE ahora comprueba ambos campos.
        // Solo se borrará la fila si la placa COINCIDE Y el usuario_id COINCIDE.
        val deleted = db.delete("vehiculos", "placa=? AND usuario_id=?", arrayOf(placa, usuarioId.toString()))
        return deleted > 0
    }

    fun obtenerVehiculoPorPlaca(placa: String, usuarioId: Int): Vehiculo? {
        val db = readableDatabase
        // La consulta busca un vehículo que coincida con la PLACA y el USUARIO_ID
        val cursor = db.rawQuery(
            "SELECT * FROM vehiculos WHERE placa = ? AND usuario_id = ?",
            arrayOf(placa, usuarioId.toString())
        )

        var vehiculo: Vehiculo? = null
        if (cursor.moveToFirst()) {
            vehiculo = Vehiculo(
                placa = cursor.getString(cursor.getColumnIndexOrThrow("placa")),
                marca = cursor.getString(cursor.getColumnIndexOrThrow("marca")),
                modelo = cursor.getString(cursor.getColumnIndexOrThrow("modelo")),
                anio = cursor.getInt(cursor.getColumnIndexOrThrow("anio")),
                color = cursor.getString(cursor.getColumnIndexOrThrow("color")),
                costoPorDia = cursor.getDouble(cursor.getColumnIndexOrThrow("costoPorDia")),
                activo = cursor.getInt(cursor.getColumnIndexOrThrow("activo")) == 1,
                imagenRes = cursor.getInt(cursor.getColumnIndexOrThrow("imagenRes"))
            )
        }
        cursor.close()
        return vehiculo
    }

    fun obtenerVehiculos(usuarioId: Int): List<Vehiculo> {
        val db = readableDatabase
        val vehiculos = mutableListOf<Vehiculo>()
        val cursor = db.rawQuery(
            "SELECT * FROM vehiculos WHERE usuario_id = ?",
            arrayOf(usuarioId.toString())
        )
        if (cursor.moveToFirst()) {
            do {
                val vehiculo = Vehiculo(
                    placa = cursor.getString(cursor.getColumnIndexOrThrow("placa")),
                    marca = cursor.getString(cursor.getColumnIndexOrThrow("marca")),
                    modelo = cursor.getString(cursor.getColumnIndexOrThrow("modelo")),
                    anio = cursor.getInt(cursor.getColumnIndexOrThrow("anio")),
                    color = cursor.getString(cursor.getColumnIndexOrThrow("color")),
                    costoPorDia = cursor.getDouble(cursor.getColumnIndexOrThrow("costoPorDia")),
                    activo = cursor.getInt(cursor.getColumnIndexOrThrow("activo")) == 1,
                    imagenRes = cursor.getInt(cursor.getColumnIndexOrThrow("imagenRes"))
                )
                vehiculos.add(vehiculo)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return vehiculos
    }
}
