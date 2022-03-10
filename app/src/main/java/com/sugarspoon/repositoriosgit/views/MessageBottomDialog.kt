package com.sugarspoon.repositoriosgit.views

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sugarspoon.repositoriosgit.R
import com.sugarspoon.repositoriosgit.databinding.MessageDialogBinding
import kotlin.properties.Delegates

class MessageBottomDialog(
    builder: Builder
): BottomSheetDialog(builder.context) {

    var actionListener: () -> Unit = { }

    private val binding: MessageDialogBinding by lazy {
        MessageDialogBinding.inflate(layoutInflater)
    }

    private val message: String = builder.message
    private val dialogTitle: Int = builder.dialogTitle
    private val textForButton: Int = builder.textForButton
    private val statusColor: Int = builder.statusColor
    private val isCancelable: Boolean = builder.isCancelable

    class Builder(val context: Context) {

        var message by Delegates.notNull<String>()
        var dialogTitle by Delegates.notNull<Int>()
        var textForButton by Delegates.notNull<Int>()
        var statusColor by Delegates.notNull<Int>()
        var isCancelable by Delegates.notNull<Boolean>()

        fun setTitle(title: Int) : Builder {
            this.dialogTitle = title
            return this
        }

        fun setMessage(message: String) : Builder {
            this.message = message
            return this
        }

        fun setButtonText(textForButton: Int = R.string.action_close) : Builder {
            this.textForButton = textForButton
            return this
        }

        fun setStatusColor(statusColor: Int = R.color.successColor) : Builder {
            this.statusColor = statusColor
            return this
        }

        fun setCancelable(isCancelable: Boolean = true) : Builder {
            this.isCancelable = isCancelable
            return this
        }

        fun build() = MessageBottomDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupView()
        setupListener()
        setCancelable(isCancelable)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        dismiss()
    }

    private fun setupView() = WindowManager.LayoutParams(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN).run {
        copyFrom(window?.attributes)
        width = WindowManager.LayoutParams.MATCH_PARENT
        height = WindowManager.LayoutParams.MATCH_PARENT
        screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        window?.attributes = this
        with(binding) {
            messageTitleTv.text = context.getString(dialogTitle)
            messageBodyTv.text = message
            messageActionBt.text = context.getString(textForButton)
            messageActionBt.setTextColor(ContextCompat.getColor(context, statusColor))
            messageStatusV.setBackgroundColor(ContextCompat.getColor(context, statusColor))
        }
    }

    private fun setupListener() = with(binding) {
        messageActionBt.setOnClickListener {
            actionListener.invoke()
            dismiss()
        }
        messageCloseIv.setOnClickListener {
            actionListener.invoke()
            dismiss()
        }
    }
}