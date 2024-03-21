package com.raihanardila.findgithub.core.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.raihanardila.findgithub.core.data.local.dao.FavoriteUserDao
import com.raihanardila.findgithub.core.data.model.FavoriteUsersModel

@Database(entities = [FavoriteUsersModel::class], version = 2, exportSchema = false)
abstract class FavoriteUserDatabase : RoomDatabase() {
    abstract fun favoriteUserDao(): FavoriteUserDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteUserDatabase? = null

        fun getDatabase(context: Context): FavoriteUserDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteUserDatabase::class.java,
                    "favorite_database"
                )
                    .addMigrations(MIGRATION_1_2) // Tambahkan migrasi dari versi 1 ke 2 di sini
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Buat tabel baru untuk versi 2
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS favorite_users_new (" +
                            "username TEXT PRIMARY KEY NOT NULL, " +
                            "avatarUrl TEXT, " +
                            "isFavorite INTEGER NOT NULL DEFAULT 0)"
                )

                // Salin data dari tabel lama ke tabel baru
                db.execSQL(
                    "INSERT INTO favorite_users_new (username, avatarUrl, isFavorite) " +
                            "SELECT username, avatarUrl, isFavorite FROM favorite_users"
                )

                // Hapus tabel lama
                db.execSQL("DROP TABLE IF EXISTS favorite_users")

                // Ubah nama tabel baru menjadi nama tabel lama
                db.execSQL("ALTER TABLE favorite_users_new RENAME TO favorite_users")
            }
        }

    }
}
