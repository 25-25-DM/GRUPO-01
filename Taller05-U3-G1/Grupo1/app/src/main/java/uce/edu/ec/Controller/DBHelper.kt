package uce.edu.ec.Controller

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.security.MessageDigest

class DBHelper(context: Context) : SQLiteOpenHelper(context, "usuarios.db", null, 3) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("""
            CREATE TABLE usuarios(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                usuario TEXT,
                contrasenia TEXT
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS usuarios")
        onCreate(db)
    }

    private fun hashPassword(password: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val bytes = md.digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun registrarUsuario(usuario: String, contrasenia: String): Boolean {
        val db = writableDatabase
        val hashedPassword = hashPassword(contrasenia)
        val values = ContentValues().apply {
            put("usuario", usuario)
            put("contrasenia", hashedPassword)
        }
        return db.insert("usuarios", null, values) != -1L
    }

    fun verificarUsuario(usuario: String, contrasenia: String): Boolean {
        val db = readableDatabase
        val hashedPassword = hashPassword(contrasenia)
        val cursor = db.rawQuery(
            "SELECT * FROM usuarios WHERE usuario=? AND contrasenia=?",
            arrayOf(usuario, hashedPassword)
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }
}
