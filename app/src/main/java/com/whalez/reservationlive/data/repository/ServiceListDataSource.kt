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
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, Service>() {

    private var itemIndexBegin = FIRST_ITEM_INDEX
    private var itemIndexEnd = POST_ITEM_COUNTS
//    private var countTo = POST_ITEM_COUNTS
    private var codeName = "축구장"

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
                        callback.onResult(it.listPublicReservationSport.serviceList, null, itemIndexBegin + POST_ITEM_COUNTS)
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
                        if(it == null){
                            Log.d("KKK", "null 이지롱")
                        }
//                        Log.d("KKK listTotalCount", it.listPublicReservationSport.listTotalCount.toString())
//                        Log.d("KKK params.key", params.key.toString())

                        if(it.listPublicReservationSport.listTotalCount >= params.key){
                            Log.d("KKK", "여기?")
                            callback.onResult(it.listPublicReservationSport.serviceList, params.key + 1)
                            networkState.postValue(NetworkState.LOADED)
                        } else {
                            Log.d("KKK", "잘 들어오니?")
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