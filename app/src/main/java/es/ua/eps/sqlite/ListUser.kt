package es.ua.eps.sqlite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListUser : AppCompatActivity() {
    private lateinit var userOperations: UserOperations

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_user)

        // Inicializar UserOperations
        userOperations = UserOperations(this)

        // RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.usersrecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Obtener datos y configurar el adaptador
        val users = userOperations.getAllUsers()

        // Verificar que hay usuarios y luego asignar el adaptador
        if (users.isNotEmpty()) {
            val adapter = UserAdapter(users)
            recyclerView.adapter = adapter
        } else {
            Log.d("ListUser", "No hay usuarios disponibles")
        }
        // Botón "BACK"
        val btnBack = findViewById<Button>(R.id.back)
        btnBack.setOnClickListener {
            val intent = Intent(this, UserManagement::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }
}
