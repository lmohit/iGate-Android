package com.application.igate.visitorDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.application.igate.model.visitor.VisitorDetailsResponse
import com.application.igate.network.RetrofitService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables

class VisitorDetailsModel {

    private var disposable = Disposables.empty()
    private val commonStateMutableLiveData = MutableLiveData<VisitorDetailsUIModel>()
    val commonStateLiveData: LiveData<VisitorDetailsUIModel>
        get() = commonStateMutableLiveData

    fun getVisitorDetails() {
        commonStateMutableLiveData.postValue(VisitorDetailsUIModel.ShowProgress(true))
        disposable =
            RetrofitService.restClient
                .getVisitorDetails()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::visitorDetailsFetched, ::visitorDetailFetchFailed)
    }

    private fun visitorDetailsFetched(response: VisitorDetailsResponse) {
        if (response.code == 200) {
            commonStateMutableLiveData
                .postValue(VisitorDetailsUIModel.VisitorDetailsFetched(response.visitors))
        }
    }

    private fun visitorDetailFetchFailed(throwable: Throwable) {
        commonStateMutableLiveData.postValue(VisitorDetailsUIModel.Error(throwable.message))
    }
}