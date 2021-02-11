package com.application.igate.network

import com.application.igate.model.visitor.BaseResponse
import com.application.igate.model.visitor.Visitor
import io.reactivex.Single
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiHelper {

    @Multipart
    @POST("/visitor/saveVisitor")
    fun addVisitor(
        @Part reqBody: Visitor
    ): Single<BaseResponse>
}