package es.ua.eps.sqlite

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

object Permisos {

    // Verificar si los permisos de escritura están concedidos
    fun checkAndRequestPermissions(activity: Activity): Boolean {
        val permissionWrite = ContextCompat.checkSelfPermission(
            activity,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        if (permissionWrite != PackageManager.PERMISSION_GRANTED) {
            // Si no se tiene el permiso, solicitarlo
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE_WRITE_EXTERNAL_STORAGE
            )
            return false
        }
        return true
    }

    // Constante para identificar la solicitud de permisos
    internal const val REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 100

    // Crear respaldo de la base de datos
    fun backupDatabase(context: Context, backupFileName: String): Boolean {
        // Verificar permisos
        if (!checkAndRequestPermissions(context as Activity)) {
            return false
        }
        try {
            // Ruta de la base de datos llamada usuario.db
            val databasePath = context.getDatabasePath("usuario.db")

            // Verificar si la base de datos existe
            if (!databasePath.exists()) {
                Toast.makeText(context, "La base de datos no existe.", Toast.LENGTH_SHORT).show()
                return false
            }
            // Directorio de destino para el respaldo
            val backupDir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "DatabaseBackups"
            )

            if (!backupDir.exists()) {
                backupDir.mkdirs() // Crear el directorio si no existe
            }

            // Crear archivo de respaldo con el nombre personalizado
            val backupFile = File(backupDir, "$backupFileName.db")
            val src = FileInputStream(databasePath)
            val dst = FileOutputStream(backupFile)

            // Copiar el contenido de la base de datos al archivo de respaldo
            dst.channel.transferFrom(src.channel, 0, src.channel.size())
            src.close()
            dst.close()

            // Notificar al usuario que el respaldo fue exitoso
            Toast.makeText(context, "Backup creado: ${backupFile.absolutePath}", Toast.LENGTH_LONG).show()
            return true
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "Error al crear el backup", Toast.LENGTH_SHORT).show()
            return false
        }
    }
}
