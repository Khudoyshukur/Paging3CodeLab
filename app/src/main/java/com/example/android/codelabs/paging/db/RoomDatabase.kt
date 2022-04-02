package com.example.android.codelabs.paging.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.android.codelabs.paging.model.Repo

/**
 * Created by: androdev
 * Date: 4/2/2022
 * Time: 3:34 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Database(
    entities = [Repo::class],
    version = 1,
    exportSchema = false
)
abstract class RepoDatabase : RoomDatabase() {
    abstract fun reposDao(): RepoDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: RepoDatabase? = null

        fun getInstance(context: Context) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }

        private fun buildDatabase(context: Context) = Room
            .databaseBuilder(context.applicationContext, RepoDatabase::class.java, "Github.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}