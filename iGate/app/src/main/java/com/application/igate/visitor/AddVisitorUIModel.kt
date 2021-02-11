package com.application.igate.visitor

sealed class AddVisitorUIModel {

    class ShowProgress(show: Boolean): AddVisitorUIModel()
}