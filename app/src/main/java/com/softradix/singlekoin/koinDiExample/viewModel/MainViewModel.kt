package com.softradix.singlekoin.koinDiExample.viewModel

import androidx.lifecycle.*
import com.softradix.singlekoin.koinDiExample.utils.Resource
import com.softradix.singlekoin.koinDiExample.model.User
import com.softradix.singlekoin.koinDiExample.repository.MainRepository
import com.softradix.singlekoin.koinDiExample.utils.NetworkHelper
import kotlinx.coroutines.launch

class MainViewModel(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val _users = MutableLiveData<Resource<List<User>>>()
    val users: LiveData<Resource<List<User>>>
        get() = _users

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            _users.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getUsers().let {
                    if (it.isSuccessful) {
                        _users.postValue(Resource.success(it.body()))
                    } else _users.postValue(Resource.error(it.errorBody().toString(), null))
                }
            } else _users.postValue(Resource.error("No internet connection", null))
        }

    }
}