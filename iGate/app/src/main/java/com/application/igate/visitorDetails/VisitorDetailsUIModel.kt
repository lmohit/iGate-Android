package com.application.igate.visitorDetails

import com.application.igate.model.visitor.Visitor

sealed class VisitorDetailsUIModel {

    class ShowProgress(val show: Boolean): VisitorDetailsUIModel()

    class Error(val msg: String?): VisitorDetailsUIModel()

    class VisitorDetailsFetched(val visitors: Array<Visitor>): VisitorDetailsUIModel()
}