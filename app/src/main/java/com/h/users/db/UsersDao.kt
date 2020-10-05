package com.h.users.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.h.users.data.User
import io.reactivex.Single

@Dao
interface UsersDao {
    @Query("SELECT* FROM users")
    fun getUsers(): LiveData<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUsers(users: List<User>)

    @Query("SELECT COUNT(1) FROM users")
    fun usersCount(): Single<List<Int>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Query("DELETE FROM users")
    fun deleteAll()

    @Query("DELETE FROM users WHERE id = :id")
    fun deleteUser(id: String)

}
