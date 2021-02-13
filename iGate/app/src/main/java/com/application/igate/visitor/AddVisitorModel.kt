package com.application.igate.visitor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.application.igate.model.visitor.BaseResponse
import com.application.igate.model.visitor.Visitor
import com.application.igate.network.RetrofitService
import com.google.gson.JsonObject
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.sql.Timestamp

class AddVisitorModel {

    private var disposable = Disposables.empty()
    private val commonStateMutableLiveData = MutableLiveData<AddVisitorUIModel>()
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
        commonStateMutableLiveData.postValue(AddVisitorUIModel.ShowProgress(true))
        disposable =
            RetrofitService.restClient
                .addVisitor(
                    getRequestBody(
                        name, number, email, address, purpose, flatNo, timestamp, photo
                    )
                ).map {
                    commonStateMutableLiveData.postValue(AddVisitorUIModel.ShowProgress(false))
                    if (it.code == 200) {
                        commonStateMutableLiveData.postValue(AddVisitorUIModel.VisitorAdded())
                    }
                    it
                }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    commonStateMutableLiveData.postValue(AddVisitorUIModel.Error(it.message))
                }
                .subscribe()
    }

    private fun getRequestBody(
        name: String,
        number: String,
        email: String,
        address: String,
        purpose: String,
        flatNo: String,
        timestamp: String,
        photo: File?
    ): Visitor {
        return Visitor(
            photo?.readBytes(),
            number,
            name,
            email,
            purpose,
            address,
            flatNo,
            timestamp
        )
    }

    companion object {
        private val TAG = AddVisitorModel::class.java.simpleName
    }
}