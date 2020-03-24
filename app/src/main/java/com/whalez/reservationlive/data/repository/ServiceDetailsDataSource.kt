package com.whalez.reservationlive.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.whalez.reservationlive.data.api.ServiceDBInterface
import com.whalez.reservationlive.data.vo.service_detail.ServiceDetails
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ServiceDetailsDataSource(
    private val apiService: ServiceDBInterface,
    private val compositeDisposable: CompositeDisposable
) {
    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _downloadedServiceDetailsResponse = MutableLiveData<ServiceDetails>()
    val downloadedServiceResponse: LiveData<ServiceDetails>
        get() = _downloadedServiceDetailsResponse

    fun fetchServiceDetails(serviceId: String){
        _networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                apiService.getServiceDetails(serviceId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _downloadedServiceDetailsResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)
                        },
                        {
                            _networkState.postValue(NetworkState.ERROR)
                            Log.e("ServiceDetailDataSource", it.message.toString())
                        }
                    )
            )
        } catch (e: Exception){
            Log.e("ServiceDetailDataSource", e.message.toString())
        }
    }
}