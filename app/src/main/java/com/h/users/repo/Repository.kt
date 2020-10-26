package com.h.users.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.h.users.data.User
import com.h.users.db.UsersDao
import com.h.users.network.Api
import com.h.users.network.ApiResponse
import com.h.users.viewmodel.ApiStatus
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class Repository @Inject constructor(
    private val apiService: Api,
    private val dao: UsersDao
) {
    private val TAG = "MainRepository"
    private var isExhausted = false
    val status: MutableLiveData<ApiStatus> = MutableLiveData(
        ApiStatus.LOADING)

    /*private val _status = repository.status
    val status: LiveData<ApiStatus>
        get() = _status*/

    val compositeDisposable = CompositeDisposable()


    fun getUsersFromDb(): LiveData<List<User>> {
        return dao.getUsers()
    }

    fun load(page: Int) {
        status.postValue(ApiStatus.LOADING)
        compositeDisposable.add(
            apiService.getUsers(page)
                .map(ApiResponse::data)
                .subscribeOn(Schedulers.io())
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
        compositeDisposable.add(
            Completable.fromAction() {
                dao.deleteAll()
            }
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { Log.d(TAG, "deleteAll OnSuccess")
                        loadIfDbIsEmpty()
                    },
                    { Log.d(TAG, "deleteAll OnError") })
        )
    }

    fun deleteUser(id: String) {
        compositeDisposable.add(
            Completable.fromAction() {
                dao.deleteUser(id)
            }
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { Log.d(TAG, "deleteUser OnSuccess") },
                    { Log.d(TAG, "deleteUser OnError") })
        )
    }

    fun saveUser(user: User) {
        compositeDisposable.add(
            Completable.fromAction() {
                dao.insertUser(user)
            }
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { Log.d(TAG, "saveUser OnSuccess") },
                    { Log.d(TAG, "saveUser OnError") })
        )
    }


    fun saveUsers(list: List<User>) {
        compositeDisposable.add(
            Completable.fromAction() {
                dao.addUsers(list)
            }
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { Log.d(TAG, "saveUsers OnSuccess") },
                    { Log.d(TAG, "saveUsers OnError") })
        )
    }

    fun loadIfDbIsEmpty() {
        val page = 1
        compositeDisposable.add(
            dao.usersCount()
                .subscribeOn(Schedulers.io())
                .subscribe({ result ->
                    if (result.isEmpty() || result.get(0) == 0) {
                        Log.d(TAG, "loadIfDbIsEmpty")
                        load(page)
                    }
                }, {
                    Log.d(TAG, it.message.toString())
                })
        )
    }
}



