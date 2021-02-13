package com.application.igate.network

import com.application.igate.model.visitor.BaseResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiHelper {

    @Multipart
    @POST("/visitor/saveVisitor")
    fun addVisitor(
        @Part visitorPhoto: MultipartBody.Part,
        @Part("phoneNumber") phoneNumber: String,
        @Part("visitorName") visitorName: String,
        @Part("visitorEmailId") visitorEmailId: String,
        @Part("visitorPurpose") visitorPurpose: String,
        @Part("visitorAddress") visitorAddress: String,
        @Part("meetingFlatNo") meetingFlatNo: String,
        @Part("visitingTime") visitingTime: String
    ): Single<BaseResponse>
}