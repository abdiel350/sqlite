package es.ua.eps.sqlite

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class UserData : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_data)

        // Referencias a las vistas
        val welcomeText: TextView = findViewById(R.id.welcome_text)
        val usernameValue: TextView = findViewById(R.id.username_label)

        // Obtener datos del intent
        val fullName = intent.getStringExtra("FULL_NAME")
        val username = intent.getStringExtra("USERNAME")

        // Asignar datos a las vistas
        welcomeText.text = "\t\t\t   Welcome:\t\t\t$fullName"
        usernameValue.text ="\t\t\t  User Name:\t\t\t$username"

        // Botón para regresar
        val btnBack = findViewById<Button>(R.id.back)
        btnBack.setOnClickListener {
            // Crear un Intent para redirigir a MainActivity
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }
}