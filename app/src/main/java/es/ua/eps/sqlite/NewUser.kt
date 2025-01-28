package es.ua.eps.sqlite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class NewUser : AppCompatActivity() {

    private lateinit var userOperations: UserOperations

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)

        // Inicializar UserOperations
        userOperations = UserOperations(this)

        // Referencias a los campos de texto y botones
        val etUsername: EditText = findViewById(R.id.loginEditText)
        val etPassword: EditText = findViewById(R.id.passwordEditText)
        val etFullName: EditText = findViewById(R.id.usernameEditText)
        val btnSave: Button = findViewById(R.id.newUserButton)
        val btnBack = findViewById<Button>(R.id.back)

        //boton back
        btnBack.setOnClickListener {
            val intent = Intent(this, UserManagement::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        // Acción para guardar el nuevo usuario
        btnSave.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            val fullName = etFullName.text.toString()


            // Validar que los campos no estén vacíos
            if (username.isNotEmpty() && password.isNotEmpty() && fullName.isNotEmpty() ) {

                Log.d("UserInput", "Username: $username, Password: $password, FullName: $fullName")

                // Llamar al método addUser para agregar el usuario
                val result = userOperations.addUser(username, password, fullName)

                // Mostrar un mensaje dependiendo de si la inserción fue exitosa
                if (result == -1L) {
                    Log.d("resultado", "Username: $result ")
                    Toast.makeText(this, "Error al crear el usuario", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Usuario creado exitosamente", Toast.LENGTH_SHORT).show()
                    // Limpiar los campos para ingresar un nuevo usuario
                    etUsername.text.clear()
                    etPassword.text.clear()
                    etFullName.text.clear()

                }
            } else {
                Toast.makeText(this, "Todos los campos son requeridos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}