package es.ua.eps.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "Usuarios.db"
        private const val DATABASE_VERSION = 2
        const val TABLE_USERS = "Usuarios"

        // Columnas
         const val COLUMN_ID = "ID"
         const val COLUMN_USERNAME = "nombre_usuario"
         const val COLUMN_PASSWORD = "password"
         const val COLUMN_FULLNAME = "nombre_completo"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USERNAME TEXT NOT NULL,
                $COLUMN_PASSWORD TEXT NOT NULL,
                $COLUMN_FULLNAME TEXT NOT NULL               
            )
        """.trimIndent()
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }
}