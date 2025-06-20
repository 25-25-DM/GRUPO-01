package uce.edu.ec.Controller

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import uce.edu.ec.Models.Vehiculo
import uce.edu.ec.R

// VERSIÓN DE LA BD INCREMENTADA A 4 para aplicar los cambios en la estructura de la tabla.
class DBHelper(context: Context) : SQLiteOpenHelper(context, "app_cache.db", null, 4) {

    override fun onCreate(db: SQLiteDatabase?) {
        // Tabla de usuarios local para mapear nombre de usuario (String) a un ID local (Int).
        db?.execSQL(
            """
            CREATE TABLE usuarios(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                usuario TEXT UNIQUE NOT NULL
            )
        """.trimIndent()
        )

        // Tabla de vehículos local, usando 'usuario_id' y con la nueva columna 'sincronizado'.
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
                usuario_id INTEGER, 
                sincronizado INTEGER DEFAULT 0, -- 0 para no, 1 para sí
                FOREIGN KEY(usuario_id) REFERENCES usuarios(id)
            )
        """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Método simple de actualización: borrar todo y recrear.
        db?.execSQL("DROP TABLE IF EXISTS vehiculos")
        db?.execSQL("DROP TABLE IF EXISTS usuarios")
        onCreate(db)
    }

    // --- FUNCIONES DE UTILIDAD PARA LA GESTIÓN LOCAL ---

    /**
     * Obtiene el ID numérico de un usuario. Si no existe localmente, lo crea y devuelve el nuevo ID.
     * Esta función es el "traductor" entre el mundo de DynamoDB (nombres) y SQLite (IDs).
     */
    fun obtenerOInsertarUsuarioLocal(usuario: String): Int {
        val db = writableDatabase
        val cursor = db.rawQuery("SELECT id FROM usuarios WHERE usuario = ?", arrayOf(usuario))

        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            cursor.close()
            return id
        }
        cursor.close()

        val values = ContentValues().apply { put("usuario", usuario) }
        return db.insert("usuarios", null, values).toInt()
    }

    fun obtenerTodosLosUsuariosLocales(): List<Pair<Int, String>> {
        val db = readableDatabase
        val usuarios = mutableListOf<Pair<Int, String>>()
        val cursor = db.rawQuery("SELECT id, usuario FROM usuarios", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("usuario"))
                usuarios.add(Pair(id, nombre))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return usuarios
    }

    fun eliminarUsuarioPorNombre(usuario: String) {
        val db = writableDatabase
        db.delete("usuarios", "usuario = ?", arrayOf(usuario))
    }

    fun eliminarTodosLosVehiculosDelUsuario(usuarioId: Int) {
        val db = writableDatabase
        db.delete("vehiculos", "usuario_id = ?", arrayOf(usuarioId.toString()))
    }

    fun obtenerPlacasDeVehiculosDelUsuario(usuarioId: Int): List<String> {
        val db = readableDatabase
        val placas = mutableListOf<String>()
        val cursor = db.rawQuery("SELECT placa FROM vehiculos WHERE usuario_id = ?", arrayOf(usuarioId.toString()))
        if (cursor.moveToFirst()) {
            do {
                placas.add(cursor.getString(cursor.getColumnIndexOrThrow("placa")))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return placas
    }

    fun marcarVehiculoComoSincronizado(placa: String, estado: Boolean) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("sincronizado", if (estado) 1 else 0)
        }
        db.update("vehiculos", values, "placa = ?", arrayOf(placa))
    }

    // --- FUNCIONES CRUD PARA VEHÍCULOS (LOCAL) ---

    fun insertarVehiculo(vehiculo: Vehiculo, usuarioId: Int, sincronizado: Boolean) {
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
            put("sincronizado", if (sincronizado) 1 else 0)
        }
        db.insertWithOnConflict("vehiculos", null, values, SQLiteDatabase.CONFLICT_REPLACE)
    }

    fun actualizarVehiculo(vehiculo: Vehiculo, usuarioId: Int, sincronizado: Boolean): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("marca", vehiculo.marca)
            put("modelo", vehiculo.modelo)
            put("anio", vehiculo.anio)
            put("color", vehiculo.color)
            put("costoPorDia", vehiculo.costoPorDia)
            put("activo", if (vehiculo.activo) 1 else 0)
            put("imagenRes", vehiculo.imagenRes)
            put("sincronizado", if (sincronizado) 1 else 0)
        }
        val rowsAffected = db.update("vehiculos", values, "placa = ? AND usuario_id = ?", arrayOf(vehiculo.placa, usuarioId.toString()))
        return rowsAffected > 0
    }

    fun eliminarVehiculo(placa: String, usuarioId: Int) {
        val db = writableDatabase
        db.delete("vehiculos", "placa = ? AND usuario_id = ?", arrayOf(placa, usuarioId.toString()))
    }

    fun obtenerVehiculos(usuarioId: Int): List<Vehiculo> {
        val db = readableDatabase
        val vehiculos = mutableListOf<Vehiculo>()
        val cursor = db.rawQuery("SELECT * FROM vehiculos WHERE usuario_id = ?", arrayOf(usuarioId.toString()))

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

    fun obtenerVehiculoPorPlaca(placa: String, usuarioId: Int): Vehiculo? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM vehiculos WHERE placa = ? AND usuario_id = ?", arrayOf(placa, usuarioId.toString()))
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
}