package com.h.users.repo

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.h.users.data.User
import com.h.users.db.UsersDao
import com.h.users.db.UsersDatabase
import com.h.users.network.Api
import com.h.users.network.ApiResponse
import com.h.users.network.ServiceGenerator
import com.h.users.ui.ApiStatus
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class Repository(application: Application) {
    private val TAG = "MainRepository"
    private var usersDao: UsersDao
    private val apiService: Api
    private var isExhausted = false
    val status: MutableLiveData<ApiStatus> = MutableLiveData(ApiStatus.LOADING)


    private val compositeDisposable = CompositeDisposable()

    init {
        val database = UsersDatabase.getDatabase(application.applicationContext)
        usersDao = database.noteDao()
        apiService = ServiceGenerator.retrofitService
    }


    fun getUsersFromDb(): LiveData<List<User>> {
        return usersDao.getUsers()
    }

    fun load(page: Int) {
        status.postValue(ApiStatus.LOADING)
        compositeDisposable.add(
            ServiceGenerator.retrofitService.getUsers(page)
                //.delay(1, TimeUnit.SECONDS)
                //.timeout(4, TimeUnit.SECONDS)
                .map(ApiResponse::data)
                .subscribeOn(Schedulers.io())
                //.observeOn(AndroidSchedulers.mainThread())
                .filter { list ->
                    Log.d(TAG, "list = $list")
                    if (list.isEmpty())
                        isExhausted = true
                    list.isNotEmpty()
                }
                .subscribe({
                    Log.d(TAG, "saveUsers = $it")
                    saveUsers(it)
                    status.postValue(ApiStatus.DONE)
                }, {
                    Log.d(TAG, it.message.toString())
                    status.postValue(ApiStatus.ERROR)
                    isExhausted = false
                })
        )
    }

    fun updateDb() {
        isExhausted = false
        deleteAll()
    }

    fun isExhausted(): Boolean {
        return isExhausted
    }

    fun deleteAll() {
        Completable.fromAction() {
            usersDao.deleteAll()
        }
            .subscribeOn(Schedulers.io())
            .subscribe(
                { Log.d(TAG, "deleteAll OnSuccess")
                    loadIfDbIsEmpty()
                },
                { Log.d(TAG, "deleteAll OnError") })
    }

    fun deleteUser(id: String) {
        Completable.fromAction() {
            usersDao.deleteUser(id)
        }
            .subscribeOn(Schedulers.io())
            .subscribe(
                { Log.d(TAG, "deleteUser OnSuccess") },
                { Log.d(TAG, "deleteUser OnError") })
    }

    fun saveUser(user: User) {
        Completable.fromAction() {
            usersDao.insertUser(user)
        }
            .subscribeOn(Schedulers.io())
            .subscribe(
                { Log.d(TAG, "saveUser OnSuccess") },
                { Log.d(TAG, "saveUser OnError") })
    }


    fun saveUsers(list: List<User>) {
        Completable.fromAction() {
            usersDao.addUsers(list)
        }
            .subscribeOn(Schedulers.io())
            .subscribe(
                { Log.d(TAG, "saveUsers OnSuccess") },
                { Log.d(TAG, "saveUsers OnError") })
    }

    fun loadIfDbIsEmpty() {
        val page = 1
        usersDao.usersCount()
            .subscribeOn(Schedulers.io())
            .subscribe({ result ->
                if (result.isEmpty() || result.get(0) == 0) {
                    Log.d(TAG, "loadIfDbIsEmpty")
                    load(page)
                }
            }, {
                    Log.d(TAG, it.message.toString())
            })
    }

}



