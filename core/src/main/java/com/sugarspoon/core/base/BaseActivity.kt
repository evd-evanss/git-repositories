package com.sugarspoon.core.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.example.core.databinding.ToolbarLayoutBinding
import com.sugarspoon.core.utils.setVisible

typealias Inflate<T> = (LayoutInflater) -> T

abstract class BaseActivity<Binding: ViewBinding>(
    private val inflater: Inflate<Binding>
): AppCompatActivity() {

    val context: Context
        get() = this

    lateinit var binding: Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inflater.run { binding = invoke(layoutInflater) }
        setContentView(binding.root)
    }

    fun setToolbar(
        text: String,
        iconCloseIsVisible: Boolean,
        viewGroup: ViewGroup
    ) = with(ToolbarLayoutBinding.bind(viewGroup)){
        toolbarText.text = text
        toolbarCloseIv.setVisible(iconCloseIsVisible)
        toolbarCloseIv.setOnClickListener {
            finish()
        }
    }

}