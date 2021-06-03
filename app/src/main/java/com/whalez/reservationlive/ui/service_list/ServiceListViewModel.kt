package com.whalez.reservationlive.ui.service_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.paging.PagingData
import com.whalez.reservationlive.data.repository.NetworkState
import com.whalez.reservationlive.data.repository.ServiceListRepository
import com.whalez.reservationlive.data.vo.service_list.Service
import io.reactivex.disposables.CompositeDisposable

class ServiceListViewModel(
    private val servicePagedListRepository: ServiceListRepository,
    codeName: String
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()


    val servicePagedList: LiveData<PagingData<Service>> by lazy {
        servicePagedListRepository.fetchLiveServicePagedList(compositeDisposable, codeName)
    }

    val networkState: LiveData<NetworkState> by lazy {
        servicePagedListRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean = servicePagedList.value == null// ?: true

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}