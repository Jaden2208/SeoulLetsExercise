package com.whalez.reservationlive.ui.service_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.whalez.reservationlive.data.repository.NetworkState
import com.whalez.reservationlive.data.vo.service_list.Service
import io.reactivex.disposables.CompositeDisposable

class MainActivityViewModel(
    private val servicePagedListRepository: ServicePagedListRepository,
    codeName: String
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()


    val servicePagedList: LiveData<PagedList<Service>> by lazy {
        servicePagedListRepository.fetchLiveServicePagedList(compositeDisposable, codeName)
    }

    val networkState: LiveData<NetworkState> by lazy {
        servicePagedListRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean = servicePagedList.value?.isEmpty() ?: true

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}