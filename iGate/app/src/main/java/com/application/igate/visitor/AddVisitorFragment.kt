package com.application.igate.visitor

import android.R.attr
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.application.igate.BaseFragment
import com.application.igate.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_add_visitor.*
import javax.inject.Inject


class AddVisitorFragment: BaseFragment() {

    private lateinit var viewModel: AddVisitorViewModel

    @Inject
    private lateinit var viewModelFactory: ViewModelProvider.Factory

    private var visitorImageUri: Uri ?= null

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
        visitor_photo.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST)
        }

        submit.setOnClickListener {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CAMERA_PIC_REQUEST -> {
                visitorImageUri = data?.getParcelableExtra(IMAGE)
                visitorImageUri?.let {
                    loadImage()
                }
            }
        }
    }

    private fun loadImage() {
        Glide.with(context!!)
            .load(visitorImageUri)
            .skipMemoryCache(true)
            .into(visitor_photo)
    }

    companion object {
        private val TAG = AddVisitorFragment::class.java
        private const val CAMERA_PIC_REQUEST = 100
        private const val IMAGE = "image"

        fun newInstance(): AddVisitorFragment {
            return AddVisitorFragment()
        }
    }
}