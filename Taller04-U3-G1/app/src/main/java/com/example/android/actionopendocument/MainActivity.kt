/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.actionopendocument

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

const val DOCUMENT_FRAGMENT_TAG = "com.example.android.actionopendocument.tags.DOCUMENT_FRAGMENT"

/**
 * Simple activity to host [ActionOpenDocumentFragment].
 */
class MainActivity : AppCompatActivity() {

    private var currentFragment: ActionOpenDocumentFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = getSharedPreferences("VioDrivePrefs", Context.MODE_PRIVATE)
        val username = prefs.getString("username", "") ?: ""
        val lastLogin = prefs.getString("last_login", "") ?: ""

        // Configurar información del usuario actual
        findViewById<TextView>(R.id.userInfoTextView).text = getString(R.string.user_info, username)
        findViewById<TextView>(R.id.lastLoginTextView).text = getString(R.string.last_login, lastLogin)

        // Mostrar historial de ingresos
        val loginHistoryContainer = findViewById<LinearLayout>(R.id.loginHistoryContainer)
        val loginHistory = JSONArray(prefs.getString("login_history", "[]"))
        
        // Invertir el orden para mostrar los más recientes primero
        val recentLogins = mutableListOf<JSONObject>()
        for (i in 0 until loginHistory.length()) {
            recentLogins.add(loginHistory.getJSONObject(i))
        }
        recentLogins.reverse()

        // Mostrar los usuarios
        recentLogins.forEach { login ->
            val historyEntry = TextView(this).apply {
                text = "${login.getString("username")} - ${login.getString("timestamp")}"
                setPadding(16, 8, 16, 8)
                background = getDrawable(R.drawable.info_background)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 0, 0, 8)
                }
            }
            loginHistoryContainer.addView(historyEntry)
        }

        // Configurar botón de inicio (home)
        findViewById<ImageButton>(R.id.logoutButton).apply {
            setImageResource(android.R.drawable.ic_menu_revert)
            setColorFilter(Color.parseColor("#8916a6"), PorterDuff.Mode.SRC_IN)
            setOnClickListener {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
        }

        // Agregar botón para limpiar historial
        val clearHistoryButton = Button(this).apply {
            text = getString(R.string.clear_history)
            setTextColor(Color.WHITE)
            background = getDrawable(R.drawable.button_background)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 16, 0, 16)
            }
            setOnClickListener {
                // Limpiar todos los datos de usuario
                prefs.edit().apply {
                    clear() // Esto limpia todas las preferencias
                    apply()
                }
                // Limpiar vista
                loginHistoryContainer.removeAllViews()
                // Mostrar mensaje
                Toast.makeText(this@MainActivity, getString(R.string.history_cleared), Toast.LENGTH_SHORT).show()
                // Regresar a la pantalla de login
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
        }
        loginHistoryContainer.addView(clearHistoryButton)

        // Configurar botón de abrir archivo
        findViewById<Button>(R.id.open_file).setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/pdf"
            }
            startActivityForResult(intent, OPEN_DOCUMENT_REQUEST)
        }

        // Configurar botón de regresar
        findViewById<ImageButton>(R.id.backButton).apply {
            setColorFilter(Color.parseColor("#8916a6"), PorterDuff.Mode.SRC_IN)
            setOnClickListener {
                showMainView()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)

        if (requestCode == OPEN_DOCUMENT_REQUEST && resultCode == Activity.RESULT_OK) {
            resultData?.data?.also { documentUri ->
                currentFragment = ActionOpenDocumentFragment.newInstance(documentUri)
                
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, currentFragment!!, DOCUMENT_FRAGMENT_TAG)
                    .commit()

                findViewById<View>(R.id.no_document_view).visibility = View.GONE
                findViewById<View>(R.id.backButton).visibility = View.VISIBLE
            }
        }
    }

    private fun showMainView() {
        findViewById<View>(R.id.no_document_view).visibility = View.VISIBLE
        findViewById<View>(R.id.backButton).visibility = View.GONE
        currentFragment?.let { fragment ->
            supportFragmentManager.beginTransaction()
                .remove(fragment)
                .commit()
        }
    }

    companion object {
        private const val OPEN_DOCUMENT_REQUEST = 0
    }
}

private const val OPEN_DOCUMENT_REQUEST_CODE = 0x33
private const val TAG = "MainActivity"
private const val LAST_OPENED_URI_KEY =
    "com.example.android.actionopendocument.pref.LAST_OPENED_URI_KEY"

