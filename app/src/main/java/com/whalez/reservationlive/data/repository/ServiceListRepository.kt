package com.whalez.reservationlive.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.*
import com.whalez.reservationlive.data.api.POST_ITEM_COUNTS
import com.whalez.reservationlive.data.api.ServiceDBInterface
import com.whalez.reservationlive.data.vo.service_list.Service
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers

class ServiceListRepository(
    private val apiService: ServiceDBInterface
) {
    private lateinit var servicePagedList: LiveData<PagingData<Service>>
    private lateinit var serviceListDataSourceFactory: ServiceListDataSourceFactory

    fun fetchLiveServicePagedList(
        compositeDisposable: CompositeDisposable,
        codeName: String
    ): LiveData<PagingData<Service>> {
        serviceListDataSourceFactory =
            ServiceListDataSourceFactory(apiService, compositeDisposable, codeName)

        val config = PagingConfig(POST_ITEM_COUNTS)

        servicePagedList = Pager(
            PagingConfig(
                config.pageSize,
                config.prefetchDistance,
                config.enablePlaceholders,
                config.initialLoadSize,
                config.maxSize
            ),
            null,
            serviceListDataSourceFactory.asPagingSourceFactory(Dispatchers.IO)
        ).liveData
        return servicePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap(
            serviceListDataSourceFactory.servicesLiveDataSource, ServiceListDataSource::networkState
        )
    }
}