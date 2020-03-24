package com.whalez.reservationlive.data.repository

import androidx.lifecycle.LiveData
import com.whalez.reservationlive.data.api.ServiceDBInterface
import com.whalez.reservationlive.data.vo.service_detail.ServiceDetails
import io.reactivex.disposables.CompositeDisposable

class ServiceDetailsRepository(private val apiService: ServiceDBInterface) {
    private lateinit var serviceDetailsDataSource: ServiceDetailsDataSource

    fun fetchSingleServiceDetails(
        compositeDisposable: CompositeDisposable,
        serviceId: String
    ): LiveData<ServiceDetails> {
        serviceDetailsDataSource =
            ServiceDetailsDataSource(apiService, compositeDisposable)
        serviceDetailsDataSource.fetchServiceDetails(serviceId)

        return serviceDetailsDataSource.downloadedServiceResponse
    }

    fun getServiceDetailsNetworkState(): LiveData<NetworkState> {
        return serviceDetailsDataSource.networkState
    }
}