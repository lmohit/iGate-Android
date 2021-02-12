package com.application.igate.visitor

sealed class AddVisitorUIModel {

    class ShowProgress(val show: Boolean): AddVisitorUIModel()

    class Error(val msg: String?): AddVisitorUIModel()

    class VisitorAdded: AddVisitorUIModel()
}