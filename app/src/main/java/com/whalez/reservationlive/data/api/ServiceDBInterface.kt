package com.whalez.reservationlive.data.api

import com.whalez.reservationlive.data.vo.service_detail.ServiceDetails
import com.whalez.reservationlive.data.vo.service_list.ServiceResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ServiceDBInterface {

    /*
     * token : 4276594b687279753132354c4e597562
     * baseURL : http://openapi.seoul.go.kr:8088/4276594b687279753132354c4e597562/json/
     * detailURL : {baseURL}/ListPublicReservationDetail/{start_index}/{end_index}/{service_id}
     * listURL : {baseURL}/ListPublicReservationSport/{start_index}/{end_index}/{축구장 or 풋살장 등..}
     */

    @GET("ListPublicReservationSport/{item_index_begin}/{item_index_end}/{code_name}")
    fun getServices(
        @Path("item_index_begin") itemIndexBegin: Int,
        @Path("item_index_end") itemIndexEnd: Int,
        @Path("code_name") codeName: String
    ) : Single<ServiceResponse>

    @GET("ListPublicReservationSport/1/1000/{code_name}")
    fun getFilteredServices(
        @Path("code_name") codeName: String
    ) : Call<ServiceResponse>

    @GET("ListPublicReservationDetail/1/1/{service_id}")
    fun getServiceDetails(@Path("service_id") serviceId: String): Single<ServiceDetails>

}