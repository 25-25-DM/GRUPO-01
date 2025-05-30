package com.example.android.actionopendocument

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*
import org.json.JSONArray
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val prefs = getSharedPreferences("VioDrivePrefs", Context.MODE_PRIVATE)
        val usernameEditText = findViewById<EditText>(R.id.usernameEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val previousUserLayout = findViewById<LinearLayout>(R.id.previousUserLayout)
        val previousUsernameText = findViewById<TextView>(R.id.previousUsernameText)
        val continueAsPreviousButton = findViewById<Button>(R.id.continueAsPreviousButton)

        // Mostrar usuario anterior si existe
        val lastUsername = prefs.getString("username", null)
        if (!lastUsername.isNullOrEmpty()) {
            previousUserLayout.visibility = View.VISIBLE
            previousUsernameText.text = lastUsername
            
            continueAsPreviousButton.setOnClickListener {
                saveLoginAndStart(lastUsername)
            }
        } else {
            previousUserLayout.visibility = View.GONE
            continueAsPreviousButton.setOnClickListener {
                Toast.makeText(this, getString(R.string.no_previous_user), Toast.LENGTH_SHORT).show()
            }
        }

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            if (username.isNotEmpty()) {
                saveLoginAndStart(username)
            } else {
                Toast.makeText(this, getString(R.string.enter_username_error), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveLoginAndStart(username: String) {
        val prefs = getSharedPreferences("VioDrivePrefs", Context.MODE_PRIVATE)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        
        prefs.edit().apply {
            putString("username", username)
            putString("last_login", currentDate)
            
            val loginHistory = JSONArray(prefs.getString("login_history", "[]"))
            val newLogin = JSONObject().apply {
                put("username", username)
                put("timestamp", currentDate)
            }
            
            if (loginHistory.length() >= 10) {
                val tempArray = JSONArray()
                for (i in 1 until loginHistory.length()) {
                    tempArray.put(loginHistory.get(i))
                }
                loginHistory.remove(0)
            }
            
            loginHistory.put(newLogin)
            putString("login_history", loginHistory.toString())
            
            apply()
        }

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
} 