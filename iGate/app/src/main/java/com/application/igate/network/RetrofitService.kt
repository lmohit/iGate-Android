package com.application.igate.network

import com.application.igate.constants.Constants
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitService {

    private const val CONNECTION_TIMEOUT = 10000L
    private var retrofit: ApiHelper? = null

    private val httpClient: OkHttpClient
        get() {
            val builder = OkHttpClient.Builder()
            builder.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
            return builder.build()
        }

    val restClient: ApiHelper
        get() {
            retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(httpClient)
                .build()
                .create(
                    ApiHelper::
                    class.java
                )
            return retrofit!!
        }
}