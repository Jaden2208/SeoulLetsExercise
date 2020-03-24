package com.whalez.reservationlive.ui.single_service_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.whalez.reservationlive.data.repository.NetworkState
import com.whalez.reservationlive.data.repository.ServiceDetailsRepository
import com.whalez.reservationlive.data.vo.service_detail.ServiceDetails
import io.reactivex.disposables.CompositeDisposable

class SingleServiceViewModel(
    private val serviceDetailsRepository: ServiceDetailsRepository,
    serviceId: String
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val serviceDetails: LiveData<ServiceDetails> by lazy {
        serviceDetailsRepository.fetchSingleServiceDetails(compositeDisposable, serviceId)
    }

    val networkState: LiveData<NetworkState> by lazy {
        serviceDetailsRepository.getServiceDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}