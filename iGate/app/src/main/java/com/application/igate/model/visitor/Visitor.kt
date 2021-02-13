package com.application.igate.model.visitor

data class Visitor(
    val visitorId: String = "",
    val visitorPhoto: String = "",
    val phoneNumber: String = "",
    val visitorName: String = "",
    val visitorEmailId: String = "",
    val visitorPurpose: String = "",
    val visitorAddress: String = "",
    val meetingFlatNo: String = "",
    val visitingTime: String = ""
)