package es.ua.eps.sqlite

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class UserOperations(context: Context) {
    private val dbHelper: UserDatabaseHelper = UserDatabaseHelper(context)

    // Método para agregar un nuevo usuario
    fun addUser(username: String, password: String, fullName: String): Long {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val values = ContentValues()
        values.put(UserDatabaseHelper.COLUMN_USERNAME, username)
        values.put(UserDatabaseHelper.COLUMN_PASSWORD, password)
        values.put(UserDatabaseHelper.COLUMN_FULLNAME, fullName)
        return db.insert(UserDatabaseHelper.TABLE_USERS, null, values)
    }

    // Método para obtener un usuario
    @SuppressLint("Range")
    fun getUser(username: String, password: String): User? {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            UserDatabaseHelper.TABLE_USERS,
            arrayOf(
                UserDatabaseHelper.COLUMN_USERNAME,
                UserDatabaseHelper.COLUMN_PASSWORD,
                UserDatabaseHelper.COLUMN_FULLNAME
            ),
            "${UserDatabaseHelper.COLUMN_USERNAME}=? AND ${UserDatabaseHelper.COLUMN_PASSWORD}=?",
            arrayOf(username, password),
            null, null, null
        )

        var user: User? = null
        if (cursor.moveToFirst()) {
            val fullName = cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_FULLNAME))
            user = User(username, fullName, password)
        }
        cursor.close()
        return user
    }

    // Método para obtener todos los usuarios
    @SuppressLint("Range")
    fun getAllUsers(): List<User> {
        val db: SQLiteDatabase = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            UserDatabaseHelper.TABLE_USERS,
            arrayOf(
                UserDatabaseHelper.COLUMN_USERNAME,
                UserDatabaseHelper.COLUMN_FULLNAME,
                UserDatabaseHelper.COLUMN_PASSWORD
            ),
            null, null, null, null, null
        )

        val users = mutableListOf<User>()
        while (cursor.moveToNext()) {
            val username = cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USERNAME))
            val fullName = cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_FULLNAME))
            val password= cursor.getString(cursor.getColumnIndex(UserDatabaseHelper.COLUMN_PASSWORD))
            users.add(User(username, fullName, password ))
        }
        cursor.close()
        return users
    }

    // Método para actualizar la información de un usuario
    fun updateUser(username: String, newPassword: String, newFullName: String): Int {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val values = ContentValues()
        values.put(UserDatabaseHelper.COLUMN_PASSWORD, newPassword)
        values.put(UserDatabaseHelper.COLUMN_FULLNAME, newFullName)

        return db.update(
            UserDatabaseHelper.TABLE_USERS,
            values,
            "${UserDatabaseHelper.COLUMN_USERNAME}=?",
            arrayOf(username)
        )
    }

    // Método para eliminar un usuario
    fun deleteUser(username: String): Int {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        return db.delete(
            UserDatabaseHelper.TABLE_USERS,
            "${UserDatabaseHelper.COLUMN_USERNAME}=?",
            arrayOf(username)
        )
    }
}

// Clase de datos del usuario
data class User(
    val username: String,
    val fullName: String,
    val password: String
)