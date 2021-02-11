package com.application.igate.visitor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.application.igate.model.visitor.Visitor
import com.application.igate.network.RetrofitService
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.io.File
import java.sql.Timestamp

class AddVisitorModel {

    private val disposable = CompositeDisposable()
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
        timestamp: Timestamp,
        photo: File
    ) {
        disposable.add(
            RetrofitService.restClient
                .addVisitor(
                    getRequestBody(
                        name, number, email, address, purpose, flatNo, timestamp, photo
                    )
                ).map {
                    if (it.status != 200) {
                        commonStateMutableLiveData.postValue(AddVisitorUIModel.Error(it.result))
                    } else {
                        commonStateMutableLiveData.postValue(AddVisitorUIModel.VisitorAdded())
                    }
                }.subscribe()
        )
    }

    private fun getRequestBody(
        name: String,
        number: String,
        email: String,
        address: String,
        purpose: String,
        flatNo: String,
        timestamp: Timestamp,
        photo: File
    ): Visitor {
        return Visitor(
            photo,
            number,
            name,
            email,
            purpose,
            address,
            flatNo,
            timestamp
        )
    }
}