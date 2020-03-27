package com.whalez.reservationlive.ui.service_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.whalez.reservationlive.data.api.POST_ITEM_COUNTS
import com.whalez.reservationlive.data.api.ServiceDBInterface
import com.whalez.reservationlive.data.repository.NetworkState
import com.whalez.reservationlive.data.repository.ServiceListDataSource
import com.whalez.reservationlive.data.repository.ServiceListDataSourceFactory
import com.whalez.reservationlive.data.vo.service_list.Service
import io.reactivex.disposables.CompositeDisposable

class ServicePagedListRepository(
    private val apiService: ServiceDBInterface
) {
    private lateinit var servicePagedList: LiveData<PagedList<Service>>
    private lateinit var serviceListDataSourceFactory: ServiceListDataSourceFactory

    fun fetchLiveServicePagedList(
        compositeDisposable: CompositeDisposable,
        codeName: String
    ): LiveData<PagedList<Service>> {
        serviceListDataSourceFactory =
            ServiceListDataSourceFactory(apiService, compositeDisposable, codeName)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_ITEM_COUNTS)
            .build()

        servicePagedList = LivePagedListBuilder(serviceListDataSourceFactory, config).build()

        return servicePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<ServiceListDataSource, NetworkState>(
            serviceListDataSourceFactory.servicesLiveDataSource, ServiceListDataSource::networkState
        )
    }
}