package com.application.igate

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.application.igate.utils.Utils
import com.application.igate.visitor.AddVisitorFragment
import com.application.igate.visitor.AddVisitorUIModel

open class BaseFragment: Fragment() {

    var isAppInForegorund = true

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = ProgressDialog(context!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        isAppInForegorund = true
    }

    override fun onPause() {
        super.onPause()
        isAppInForegorund = false
    }

    fun showProgress(show: Boolean, msg: String) {
        if (show) {
            progressDialog.setMessage(msg)
            progressDialog.show()
        } else {
            Log.d(TAG, "showProgress dismiss")
            progressDialog.dismiss()
        }
    }

    fun showErrorMsg(msg: String) {
        Toast.makeText(
            context!!,
            msg,
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        private val TAG = BaseFragment::class.java.simpleName
    }
}