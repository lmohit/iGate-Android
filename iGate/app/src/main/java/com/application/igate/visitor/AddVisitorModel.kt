package com.application.igate.visitor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.application.igate.model.visitor.Visitor
import com.application.igate.network.RetrofitService
import com.google.gson.JsonObject
import io.reactivex.disposables.CompositeDisposable
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
        timestamp: String,
        photo: File?
    ) {
        disposable.add(
            RetrofitService.restClient
                .addVisitor(
                    getRequestBody(
                        name, number, email, address, purpose, flatNo, timestamp, photo
                    )
                ).map {
                    Log.d(TAG, "" + it)
                    if (it.code == 200) {
                        commonStateMutableLiveData.postValue(AddVisitorUIModel.VisitorAdded())
                    } else {
                        commonStateMutableLiveData.postValue(AddVisitorUIModel.Error(it.result))
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
        timestamp: String,
        photo: File?
    ): Visitor {
        return Visitor(
            null,
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