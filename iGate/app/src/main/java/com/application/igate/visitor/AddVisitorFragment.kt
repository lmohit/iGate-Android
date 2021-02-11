package com.application.igate.visitor

import android.Manifest
import android.R.attr
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.application.igate.BaseFragment
import com.application.igate.R
import com.application.igate.SplashActivity
import com.application.igate.factory.ViewModelFactory
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_add_visitor.*
import javax.inject.Inject


class AddVisitorFragment: BaseFragment() {

    private lateinit var viewModel: AddVisitorViewModel

    private var viewModelFactory = ViewModelFactory()

    private var visitorImageBitmap: Bitmap ?= null

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
        initViews()
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
        
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(activity!!,
           arrayOf(Manifest.permission.CAMERA),
            PERMISSION_REQUEST_CODE);
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