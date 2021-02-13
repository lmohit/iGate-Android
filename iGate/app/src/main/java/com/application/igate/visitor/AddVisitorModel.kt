package com.application.igate.visitor

import android.webkit.MimeTypeMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.application.igate.model.visitor.BaseResponse
import com.application.igate.network.RetrofitService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

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
                    prepareProfilePhotoPart(photo),
                    number, name, email, purpose, address, flatNo, timestamp
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::visitorAdded, ::visitorSaveFailed)
    }

    private fun visitorAdded(response: BaseResponse) {
        if (response.code == 200) {
            commonStateMutableLiveData.postValue(AddVisitorUIModel.VisitorAdded())
        }
    }

    private fun visitorSaveFailed(throwable: Throwable) {
        commonStateMutableLiveData.postValue(AddVisitorUIModel.Error(throwable.message))
    }

    private fun prepareProfilePhotoPart(profilePhoto: File?): MultipartBody.Part {
        val requestFile: RequestBody = RequestBody.create(
            MediaType.parse("multipart/form-data"),
            profilePhoto
        )
        return MultipartBody.Part.createFormData(VISITOR_PHOTO, profilePhoto?.name, requestFile)
    }

    private fun getMimeType(uri: String): String {
        val extension = MimeTypeMap.getFileExtensionFromUrl(uri)
        var type = ""
        extension?.let {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension).orEmpty()
        }
        return type
    }

    companion object {
        private val TAG = AddVisitorModel::class.java.simpleName
        private const val VISITOR_PHOTO = "visitorPhoto"
    }
}