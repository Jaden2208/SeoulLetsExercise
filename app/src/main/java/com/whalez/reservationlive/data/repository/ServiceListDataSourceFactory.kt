package com.whalez.reservationlive.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.whalez.reservationlive.data.api.ServiceDBInterface
import com.whalez.reservationlive.data.vo.service_list.Service
import io.reactivex.disposables.CompositeDisposable

class ServiceListDataSourceFactory(
    private val apiService: ServiceDBInterface,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, Service>() {

    val servicesLiveDataSource = MutableLiveData<ServiceListDataSource>()

    override fun create(): DataSource<Int, Service> {
        val serviceListDataSource = ServiceListDataSource(apiService, compositeDisposable)
        servicesLiveDataSource.postValue(serviceListDataSource)
        return serviceListDataSource
    }
}