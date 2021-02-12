package com.application.igate.network

import com.application.igate.model.visitor.BaseResponse
import com.application.igate.model.visitor.Visitor
import io.reactivex.Single
import retrofit2.http.*

interface ApiHelper {

    @POST("/visitor/saveVisitor")
    fun addVisitor(
        @Body reqBody: Visitor
    ): Single<BaseResponse>
}