package com.application.igate.visitor

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import java.io.File
import java.sql.Timestamp

class AddVisitorViewModel : ViewModel() {

    private var addVisitorModel: AddVisitorModel = AddVisitorModel()
    private val commonStateMutableLiveData = addVisitorModel.commonStateLiveData
    val commonStateLiveData: LiveData<AddVisitorUIModel>
        get() = commonStateMutableLiveData

    fun addVisitor(
        name: String,
        number: String,
        email: String,
        address: String,
        purpose: String,
        flatNo: String,
        timestamp: String,
        photo: File?
    ) {
        addVisitorModel
            .addVisitor(name, number, email, address, purpose, flatNo, timestamp, photo)
    }
}