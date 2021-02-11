package com.application.igate.visitor

sealed class AddVisitorUIModel {

    class ShowProgress(show: Boolean): AddVisitorUIModel()

    class Error(msg: String): AddVisitorUIModel()

    class VisitorAdded(): AddVisitorUIModel()
}