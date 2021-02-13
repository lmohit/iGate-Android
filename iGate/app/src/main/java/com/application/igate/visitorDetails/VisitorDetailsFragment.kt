package com.application.igate.visitorDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.igate.BaseFragment
import com.application.igate.R
import com.application.igate.factory.ViewModelFactory
import com.application.igate.model.visitor.Visitor
import com.application.igate.visitor.AddVisitorFragment
import com.application.igate.visitorDetails.adapter.VisitorDetailsAdapter
import kotlinx.android.synthetic.main.fragment_visitor_details.*

class VisitorDetailsFragment : BaseFragment() {

    private lateinit var visitorDetailsAdapter: VisitorDetailsAdapter
    private lateinit var viewModel: VisitorDetailsViewModel
    private var viewModelFactory = ViewModelFactory()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return LayoutInflater.from(context!!)
            .inflate(R.layout.fragment_visitor_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(VisitorDetailsViewModel::class.java)
        viewModel.commonStateLiveData.observe(this, Observer(::handleCommonStateData))
        viewModel.getVisitorDetails()
        initListeners()
    }

    private fun initListeners() {
        refreshLayout.setOnRefreshListener {
            viewModel.getVisitorDetails()
        }
    }

    private fun handleCommonStateData(uiModel: VisitorDetailsUIModel) {
        when (uiModel) {
            is VisitorDetailsUIModel.ShowProgress -> {
                showProgress(uiModel.show, getString(R.string.loading))
            }

            is VisitorDetailsUIModel.Error -> {
                showProgress(false, getString(R.string.loading))
                showErrorMsg(uiModel.msg.orEmpty())
            }

            is VisitorDetailsUIModel.VisitorDetailsFetched -> {
                initAdapter(uiModel.visitors)
            }
        }
    }

    private fun initAdapter(visitors: Array<Visitor>) {
        visitorDetailsAdapter = VisitorDetailsAdapter(context!!, visitors)
        visitor_details_recycler_view.adapter = visitorDetailsAdapter
    }

    companion object {
        private val TAG = VisitorDetailsFragment::class.java.simpleName

        fun newInstance(): VisitorDetailsFragment {
            return VisitorDetailsFragment()
        }
    }
}