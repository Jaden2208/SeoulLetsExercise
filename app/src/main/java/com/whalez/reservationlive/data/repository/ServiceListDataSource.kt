package com.whalez.reservationlive.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.whalez.reservationlive.data.api.FIRST_ITEM_INDEX
import com.whalez.reservationlive.data.api.POST_ITEM_COUNTS
import com.whalez.reservationlive.data.api.ServiceDBInterface
import com.whalez.reservationlive.data.vo.service_list.Service
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ServiceListDataSource(
    private val apiService: ServiceDBInterface,
    private val compositeDisposable: CompositeDisposable,
    private val codeName: String
) : PageKeyedDataSource<Int, Service>() {

    private var itemIndexBegin = FIRST_ITEM_INDEX
    private var itemIndexEnd = POST_ITEM_COUNTS

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Service>
    ) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.getServices(itemIndexBegin, itemIndexEnd, codeName)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.listPublicReservationSport.serviceList, null, itemIndexEnd+1)
                        networkState.postValue(NetworkState.LOADED)
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("ServiceListDataSource", it.message.toString())
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Service>) {
        networkState.postValue(NetworkState.LOADING)

        Log.d("loadAfter", params.key.toString())

        compositeDisposable.add(
            apiService.getServices(params.key, params.key, codeName)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        // 아래 조건문 warning 무시할 것, 저거 없으면 오류!
                        if(it.listPublicReservationSport != null && it.listPublicReservationSport.listTotalCount >= params.key){
                            callback.onResult(it.listPublicReservationSport.serviceList, params.key + 1)
                            networkState.postValue(NetworkState.LOADED)
                        } else {
                            networkState.postValue(NetworkState.ENDOFLIST)
                        }
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("ServiceListDataSource", it.message.toString())
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Service>) {
    }
}