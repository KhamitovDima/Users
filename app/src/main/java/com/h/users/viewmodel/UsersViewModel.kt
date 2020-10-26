package com.h.users.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.h.users.data.User
import com.h.users.repo.Repository
import javax.inject.Inject

enum class ApiStatus { LOADING, ERROR, DONE }

class UsersViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val TAG = "UsersViewModel"
    private var page = 1

    private val _status = repository.status
    val status: LiveData<ApiStatus>
        get() = _status

    fun getUsers(): LiveData<List<User>> {
        return repository.getUsersFromDb()
    }

    fun updateUsers() {
        page = 1
        repository.updateDb()
    }

    fun searchNextPage() {
        if (!repository.isExhausted()) {
            Log.d(TAG, "load in page = $page")
            repository.load(++page)
        }
    }

    fun deleteUser(id: String) {
        repository.deleteUser(id)
    }

    fun saveUser(user: User) {
        repository.saveUser(user)
    }

    fun loadIfDbIsEmpty() {
            Log.d(TAG, "loadIfDbIsEmpty")
            repository.loadIfDbIsEmpty()
    }

    override fun onCleared() {
        repository.compositeDisposable.clear()
        super.onCleared()
    }

}



