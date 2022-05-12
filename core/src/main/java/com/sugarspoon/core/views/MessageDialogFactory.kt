package com.sugarspoon.core.views

import android.content.Context
import com.example.core.R

class MessageDialogFactory(val context: Context) {

//    fun createSuccess() =
//        MessageBottomDialog.Builder(context)
//            .setTitle()
//            .setMessage()
//            .setButtonText()
//            .setStatusColor()
//            .setCancelable()
//            .build()

    fun createError(message: String) =
        MessageBottomDialog.Builder(context)
            .setTitle(R.string.ops)
            .setMessage(message)
            .setButtonText(R.string.action_try_again)
            .setStatusColor(R.color.red)
            .setCancelable(false)
            .build()
}