package com.application.igate.visitor

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.igate.BaseFragment
import com.application.igate.R
import com.application.igate.event.AddVisitor
import com.application.igate.event.RxBus
import com.application.igate.factory.ViewModelFactory
import com.application.igate.utils.Utils
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_add_visitor.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.sql.Timestamp


class AddVisitorFragment : BaseFragment() {

    private lateinit var viewModel: AddVisitorViewModel

    private var viewModelFactory = ViewModelFactory()

    private var visitorImageBitmap: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return LayoutInflater.from(context!!)
            .inflate(R.layout.fragment_add_visitor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(AddVisitorViewModel::class.java)
        viewModel.commonStateLiveData.observe(viewLifecycleOwner, Observer(::handleCommonStateData))
        initViews()
    }

    private fun handleCommonStateData(uiModel: AddVisitorUIModel) {
        when (uiModel) {
            is AddVisitorUIModel.ShowProgress -> {
                Utils.showProgress(uiModel.show)
            }

            is AddVisitorUIModel.Error -> {
                showErrorMsg(uiModel)
            }

            is AddVisitorUIModel.VisitorAdded -> {
                Toast.makeText(
                    context!!,
                    resources.getString(R.string.visitor_added_suucessfully),
                    Toast.LENGTH_SHORT
                ).show()
                RxBus.publish(AddVisitor())
            }
        }
    }

    private fun showErrorMsg(uiModel: AddVisitorUIModel.Error) {
        Toast.makeText(
            context!!,
            uiModel.msg,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun initViews() {
        Log.d(TAG, "initViews")
        visitor_photo.setOnClickListener {
            if (checkPermissions()) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST)
            } else {
                requestPermission()
            }
        }

        submit.setOnClickListener {
            saveVistorInfo()
        }
    }

    private fun saveVistorInfo() {
        val visitorName = visitor_name.text.toString()
        val visitorNumber = visitor_number.text.toString()
        val visitorEmail = visitor_email.text.toString()
        val visitorAddress = visitor_address.text.toString()
        val visitorPurpose = visitor_purpose.text.toString()
        val visitorMeeting = visitor_meeting.text.toString()
        getVisitorPhoto()
        val visitorPhoto = visitorImageBitmap?.compress(
            Bitmap.CompressFormat.PNG,
            0,
            ByteArrayOutputStream()
        )
        if (TextUtils.isEmpty(visitorName)) {
            Toast.makeText(context!!, resources.getString(R.string.enter_name), Toast.LENGTH_SHORT).show()
            return
        }
        if (TextUtils.isEmpty(visitorNumber)) {
            Toast.makeText(context!!, resources.getString(R.string.enter_number), Toast.LENGTH_SHORT).show()
            return
        }
        if (TextUtils.isEmpty(visitorPurpose)) {
            Toast.makeText(context!!, resources.getString(R.string.enter_purpose), Toast.LENGTH_SHORT).show()
            return
        }
        if (TextUtils.isEmpty(visitorMeeting)) {
            Toast.makeText(context!!, resources.getString(R.string.enter_meeting), Toast.LENGTH_SHORT).show()
            return
        }
        if (visitorPhoto == null) {
            Toast.makeText(context!!, resources.getString(R.string.take_photo), Toast.LENGTH_SHORT).show()
            return
        }
        val time = System.currentTimeMillis()
        val timestamp = Timestamp(time)
        viewModel.addVisitor(
            visitorName,
            visitorNumber,
            visitorEmail,
            visitorAddress,
            visitorPurpose,
            visitorMeeting,
            timestamp,
            getVisitorPhoto()
        )
    }

    private fun getVisitorPhoto(): File? {
//        val file = File(Environment.getExternalStorageState(), "profilePicture" + ".png")
//        val fOut = FileOutputStream(file)
//        visitorImageBitmap?.compress(Bitmap.CompressFormat.PNG, 85, fOut)
//        return file
        return null
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            activity!!,
            arrayOf(Manifest.permission.CAMERA),
            PERMISSION_REQUEST_CODE
        );
    }

    private fun checkPermissions(): Boolean {
        return (ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED
                )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CAMERA_PIC_REQUEST -> {
                visitorImageBitmap = data?.getParcelableExtra(IMAGE)
                visitorImageBitmap?.let {
                    loadImage()
                }
            }
        }
    }

    private fun loadImage() {
        Glide.with(context!!)
            .load(visitorImageBitmap)
            .skipMemoryCache(true)
            .into(visitor_photo)
    }

    companion object {
        private val TAG = AddVisitorFragment::class.java.simpleName
        private const val CAMERA_PIC_REQUEST = 100
        private const val IMAGE = "data"
        private const val PERMISSION_REQUEST_CODE = 200

        fun newInstance(): AddVisitorFragment {
            return AddVisitorFragment()
        }
    }
}