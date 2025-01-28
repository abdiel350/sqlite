package es.ua.eps.sqlite

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class UserManagement : AppCompatActivity() {
    private lateinit var userOperations: UserOperations
    private lateinit var userSpinner: Spinner
    private var selectedUser: User? = null

    private val updateUserLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                resetSpinnerToDefault()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_management)
        userOperations = UserOperations(this)
        userSpinner = findViewById(R.id.userspinner)
        loadUsersIntoSpinner()

        val btnNewUser = findViewById<Button>(R.id.newUser)
        btnNewUser.setOnClickListener {
            val intent = Intent(this, NewUser::class.java)
            startActivity(intent)
        }

        val btnUpdateUser = findViewById<Button>(R.id.updateUser)
        btnUpdateUser.setOnClickListener {
            selectedUser?.let { user ->
                val intent = Intent(this, ClientActualize::class.java)
                intent.putExtra("username", user.username) // Línea nueva: pasar username
                intent.putExtra("fullName", user.fullName) // Línea nueva: pasar fullName
                intent.putExtra("password", user.password) // Línea nueva: pasar password
                updateUserLauncher.launch(intent)
            } ?: run {
                showToast("Por favor, selecciona un usuario primero")
            }
        }

        val btnDeleteUser = findViewById<Button>(R.id.deleteUser)
        btnDeleteUser.setOnClickListener {
            selectedUser?.let { user ->
                showDeleteConfirmationDialog(user)
            } ?: run {
                showToast("Por favor, selecciona un usuario primero")
            }
        }

        val btnListUsers = findViewById<Button>(R.id.listUsers)
        btnListUsers.setOnClickListener {
            val intent = Intent(this, ListUser::class.java)
            startActivity(intent)
        }

        val btnBack = findViewById<Button>(R.id.back)
        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        resetSpinnerToDefault()
    }

    private fun loadUsersIntoSpinner() {
        val users = userOperations.getAllUsers()
        val userNames = mutableListOf("Selecciona")
        userNames.addAll(users.map { it.username })

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, userNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        userSpinner.adapter = adapter

        userSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedUser = if (position == 0) null else users[position - 1]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedUser = null
            }
        }
        userSpinner.setSelection(0)
    }

    private fun resetSpinnerToDefault() {
        loadUsersIntoSpinner()
        userSpinner.post {
            userSpinner.setSelection(0, false)
        }
        selectedUser = null
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showDeleteConfirmationDialog(user: User) {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Delete User?")
            .setMessage("Do you really want to delete the selected user?")
            .setPositiveButton("OK") { _, _ ->
                userOperations.deleteUser(user.username)
                resetSpinnerToDefault()
                showToast("Usuario eliminado correctamente")
            }
            .setNegativeButton("CANCEL") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create()
        dialog.show()
    }
}