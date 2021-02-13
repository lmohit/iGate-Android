package com.application.igate.visitorDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class VisitorDetailsViewModel: ViewModel() {

    private var visitorDetailsModel: VisitorDetailsModel = VisitorDetailsModel()
    private val commonStateMutableLiveData = visitorDetailsModel.commonStateLiveData
    val commonStateLiveData: LiveData<VisitorDetailsUIModel>
        get() = commonStateMutableLiveData

    fun getVisitorDetails() {
        visitorDetailsModel.getVisitorDetails()
    }
}