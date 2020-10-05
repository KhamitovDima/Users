package com.h.users.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.h.users.data.User

@Database(entities = [User::class], version = 1)
abstract class UsersDatabase : RoomDatabase() {

    abstract fun noteDao(): UsersDao

    companion object {
        private lateinit var instance: UsersDatabase

        fun getDatabase(context: Context): UsersDatabase {
            Log.w("DB", "getDB")
            if (!Companion::instance.isInitialized) {
                Log.w("DB", "getisInitialized")
                synchronized(UsersDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        UsersDatabase::class.java, "users")
                        .build()
                }
            }
            return instance
        }
    }
}

