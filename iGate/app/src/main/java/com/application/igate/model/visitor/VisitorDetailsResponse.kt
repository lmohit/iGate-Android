package com.application.igate.model.visitor

import com.google.gson.annotations.SerializedName

class VisitorDetailsResponse: BaseResponse() {

    @SerializedName("result")
    val visitors: Array<Visitor> = emptyArray()
}