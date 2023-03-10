package com.app.func.view.seekbarcustom.crollerTest

interface OnCrollerChangeListener {

    fun onProgressChanged(croller: Croller?, progress: Int)

    fun onStartTrackingTouch(croller: Croller?)

    fun onStopTrackingTouch(croller: Croller?)
}