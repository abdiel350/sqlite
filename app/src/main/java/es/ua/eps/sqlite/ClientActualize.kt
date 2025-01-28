package es.ua.eps.sqlite

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ClientActualize : AppCompatActivity() {
    private lateinit var userOperations: UserOperations
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var fullNameEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_udapte_user)

        userOperations = UserOperations(this)

        usernameEditText = findViewById(R.id.loginEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        fullNameEditText = findViewById(R.id.usernameEditText)

        // Obtener datos del intent
        val originalUsername = intent.getStringExtra("username")
        val originalFullName = intent.getStringExtra("fullName")
        val originalPassword = intent.getStringExtra("password")

        // Cargar datos en los campos
        usernameEditText.setText(originalUsername)
        fullNameEditText.setText(originalFullName)
        passwordEditText.setText(originalPassword)

        val btnUpdateUser = findViewById<Button>(R.id.useractualizar)
        btnUpdateUser.setOnClickListener {
            val newUsername = usernameEditText.text.toString()
            val newPassword = passwordEditText.text.toString()
            val newFullName = fullNameEditText.text.toString()

            if (newUsername.isBlank() || newPassword.isBlank() || newFullName.isBlank()) {
                Toast.makeText(this, "Todos los campos deben estar completos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (originalUsername != null && originalUsername != newUsername) {
                userOperations.deleteUser(originalUsername)
                userOperations.addUser(newUsername, newPassword, newFullName)
                Toast.makeText(this, "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show()
            } else {
                if (originalUsername != null) {
                    userOperations.updateUser(originalUsername, newPassword, newFullName)
                    Toast.makeText(this, "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show()
                }
            }

            finish()
        }

        val btnBack = findViewById<Button>(R.id.back)
        btnBack.setOnClickListener {
            finish()
        }
    }
}
