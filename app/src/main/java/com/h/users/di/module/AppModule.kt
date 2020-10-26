package com.h.users.di.module

import android.app.Application
import androidx.room.Room
import com.h.users.db.UsersDao
import com.h.users.db.UsersDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppModule{

    @Provides
    @Singleton
    fun provideDatabase(app: Application): UsersDatabase = Room
        .databaseBuilder(app, UsersDatabase::class.java, "users")
        .build()

    @Provides
    @Singleton
    fun provideUsersDao(database: UsersDatabase): UsersDao = database.noteDao()
}
