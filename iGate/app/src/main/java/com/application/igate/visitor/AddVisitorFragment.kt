package com.application.igate.visitor

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
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
import java.io.File
import java.sql.Timestamp


class AddVisitorFragment : BaseFragment() {

    private lateinit var viewModel: AddVisitorViewModel

    private var viewModelFactory = ViewModelFactory()

    private var visitorImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val builder: StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
    }

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
                showProgress(uiModel.show, getString(R.string.loading))
            }

            is AddVisitorUIModel.Error -> {
                showProgress(false, getString(R.string.loading))
                showErrorMsg(uiModel.msg.orEmpty())
            }

            is AddVisitorUIModel.VisitorAdded -> {
                showProgress(false, getString(R.string.loading))
                Toast.makeText(
                    context!!,
                    resources.getString(R.string.visitor_added_suucessfully),
                    Toast.LENGTH_SHORT
                ).show()
                RxBus.publish(AddVisitor())
            }
        }
    }

    private fun initViews() {
        Log.d(TAG, "initViews")
        visitor_photo.setOnClickListener {
            if (checkPermissions()) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                val profilePhoto =
                    File(Environment.getExternalStorageDirectory(), PROFILE_IMAGE_URI)
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(profilePhoto))
                visitorImageUri = Uri.fromFile(profilePhoto)
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
        val visitorPhoto = getVisitorPhoto()
        if (TextUtils.isEmpty(visitorName)) {
            Toast.makeText(context!!, resources.getString(R.string.enter_name), Toast.LENGTH_SHORT)
                .show()
            return
        }
        if (TextUtils.isEmpty(visitorNumber)) {
            Toast.makeText(
                context!!,
                resources.getString(R.string.enter_number),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (TextUtils.isEmpty(visitorPurpose)) {
            Toast.makeText(
                context!!,
                resources.getString(R.string.enter_purpose),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (TextUtils.isEmpty(visitorMeeting)) {
            Toast.makeText(
                context!!,
                resources.getString(R.string.enter_meeting),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (visitorPhoto == null) {
            Toast.makeText(context!!, resources.getString(R.string.take_photo), Toast.LENGTH_SHORT)
                .show()
            return
        }
        val time = System.currentTimeMillis()
        val timestamp = Timestamp(time).toString()
        viewModel.addVisitor(
            visitorName,
            visitorNumber,
            visitorEmail,
            visitorAddress,
            visitorPurpose,
            visitorMeeting,
            timestamp,
            visitorPhoto
        )
    }

    private fun getVisitorPhoto(): File? {
        return File(visitorImageUri?.path.orEmpty())
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            activity!!,
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            PERMISSION_REQUEST_CODE
        );
    }

    private fun checkPermissions(): Boolean {
        val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(context!!, permission)
                != PackageManager.PERMISSION_GRANTED
            ) return false
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CAMERA_PIC_REQUEST -> {
                Log.d(TAG, "URI : " + visitorImageUri)
                visitorImageUri.let {
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        activity?.contentResolver,
                        it
                    )
                    loadImage(bitmap)
                }
            }
        }
    }

    private fun loadImage(bitmap: Bitmap) {
        Glide.with(context!!)
            .load(bitmap)
            .skipMemoryCache(true)
            .into(visitor_photo)
    }

    companion object {
        private val TAG = AddVisitorFragment::class.java.simpleName
        private const val CAMERA_PIC_REQUEST = 100
        private const val PERMISSION_REQUEST_CODE = 200
        private const val PROFILE_IMAGE_URI = "ProfiePicture.jpg"

        fun newInstance(): AddVisitorFragment {
            return AddVisitorFragment()
        }
    }
}