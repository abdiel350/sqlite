package es.ua.eps.sqlite

import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var userOperations: UserOperations
    private var backupFileName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Crear una instancia de UserOperations
        userOperations = UserOperations(this)

        val etUsername: EditText = findViewById(R.id.username)
        val etPassword: EditText = findViewById(R.id.password)
        val btnLogin: Button = findViewById(R.id.login)
        val closeButton: Button = findViewById(R.id.close)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Todos los campos son requeridos", Toast.LENGTH_SHORT).show()
            } else {
                val user = userOperations.getUser(username, password)

                if (user != null) {
                    val intent = Intent(this, UserData::class.java)
                    intent.putExtra("FULL_NAME", user.fullName)
                    intent.putExtra("USERNAME", user.username)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Error: usuario/password incorrectos", Toast.LENGTH_SHORT).show()
                }
            }
        }
        closeButton.setOnClickListener { finish() }
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        // Forzar colores para los elementos del menú
        for (i in 0 until (menu?.size() ?: 0)) {
            val menuItem = menu?.getItem(i)
            val spannableTitle = SpannableString(menuItem?.title)
            spannableTitle.setSpan(ForegroundColorSpan(Color.BLACK), 0, spannableTitle.length, 0)
            menuItem?.title = spannableTitle
        }
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.create_backup -> {
                backupFileName = "Usuarios"
                createBackup()
                true
            }

            R.id.restore_backup -> {
                restoreBackup()
                true
            }

            R.id.manage_users -> {
                manageUsers()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

     private fun createBackup() {
        backupFileName?.let { fileName ->
            try {
                val resolver = contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.Files.FileColumns.DISPLAY_NAME, "$fileName.db")
                    put(MediaStore.Files.FileColumns.MIME_TYPE, "application/x-sqlite3")
                    put(MediaStore.Files.FileColumns.RELATIVE_PATH, "Download/DatabaseBackups")
                }

                // Insertar en MediaStore para crear el archivo
                val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
                if (uri != null) {
                    val outputStream = resolver.openOutputStream(uri)
                    val databasePath = getDatabasePath("Usuarios.db")

                    if (databasePath.exists()) {
                        val inputStream = databasePath.inputStream()
                        outputStream?.use { output ->
                            inputStream.copyTo(output)
                        }
                        Toast.makeText(
                            this,
                            "Backup creado exitosamente en Descargas/DatabaseBackups.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(this, "La base de datos no existe.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Error al crear el archivo de backup.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Error al crear el backup: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun restoreBackup() {
        // Abrir el selector de archivos para elegir el archivo de respaldo desde ubicaciones públicas
        filePickerLauncher.launch("*/*")
    }

    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            try {
                // Ruta de la base de datos actual (área privada)
                val databasePath = File("/data/data/es.ua.eps.sqlite/databases/Usuarios.db")

                // Asegurando que la base de datos exista
                if (!databasePath.exists()) {
                    databasePath.parentFile?.mkdirs()
                    databasePath.createNewFile()
                }

                // Obtener el InputStream del archivo de respaldo seleccionado
                val inputStream = contentResolver.openInputStream(uri)

                // Copiar los datos desde el archivo de respaldo al archivo de la base de datos
                if (inputStream != null) {
                    databasePath.outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                    Toast.makeText(
                        this,
                        "Backup restaurado exitosamente.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "No se pudo abrir el archivo de respaldo.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Error al restaurar el backup: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "No se seleccionó ningún archivo.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun manageUsers() {
        val intent = Intent(this, UserManagement::class.java)
        startActivity(intent)
    }
}