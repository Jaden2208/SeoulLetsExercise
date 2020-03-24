package com.whalez.reservationlive.data.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


const val API_KEY = "4276594b687279753132354c4e597562"
const val BASE_URL = "http://openapi.seoul.go.kr:8088/$API_KEY/json/"

const val FIRST_ITEM_INDEX = 1
const val POST_ITEM_COUNTS = 20

object ServiceDBClient {

    fun getClient(): ServiceDBInterface {
        val requestInterceptor = Interceptor {
            val url = it.request()
                .url()
                .newBuilder()
                .build()

            val request = it.request()
                .newBuilder()
                .url(url)
                .build()

            return@Interceptor it.proceed(request)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ServiceDBInterface::class.java)
    }
}