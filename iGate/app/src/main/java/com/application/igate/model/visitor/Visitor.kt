package com.application.igate.model.visitor

import java.io.File
import java.sql.Timestamp

data class Visitor(
    private val visitorPhoto: ByteArray?,
    private val phoneNumber: String,
    private val visitorName: String,
    private val visitorEmailId: String,
    private val visitorPurpose: String,
    private val visitorAddress: String,
    private val meetingFlatNo: String,
    private val visitingTime: String
)