package com.sugarspoon.repositoriosgit.views

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import android.view.*
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
import android.view.animation.Animation
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_TOUCH
import com.sugarspoon.repositoriosgit.R
import com.sugarspoon.repositoriosgit.databinding.DialogLoadingBinding
import com.sugarspoon.repositoriosgit.utils.darkStatusBar
import com.sugarspoon.repositoriosgit.utils.transparentStatusBar

class DialogLoading(private val canBeCancelable: Boolean = false) : DialogFragment() {

    private var isShowing = false
    lateinit var binding: DialogLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, 0)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogLoadingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.setWindowAnimations(R.style.DialogLoading_Animation)
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        //hideSystemUI()
//        WindowManager.LayoutParams(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN).run {
//            copyFrom(dialog?.window?.attributes)
//            width = WindowManager.LayoutParams.MATCH_PARENT
//            height = WindowManager.LayoutParams.MATCH_PARENT
//            screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//            dialog?.window?.attributes = this
//        }
//        if (dialog != null && dialog?.window != null) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                dialog?.window?.setDecorFitsSystemWindows(false);
//            } else {
//                if (getActivity() != null) {
//                    getActivity()?.getWindow()?.getDecorView()?.getSystemUiVisibility()
//                        ?.let { dialog?.window?.decorView?.setSystemUiVisibility(it) }
//                }
//            }
//        }
        dialog?.window?.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        dialog?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
        dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
        dialog?.window?.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.TOP)
        dialog?.window?.transparentStatusBar()
        dialog?.window?.darkStatusBar()
        setAnimation()
        binding.loading.playAnimation()
    }

    private fun hideSystemUI() = dialog?.window?.run {
        WindowCompat.setDecorFitsSystemWindows(this, false)
        WindowInsetsControllerCompat(this, binding.root).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun showSystemUI() = dialog?.window?.run {
        WindowCompat.setDecorFitsSystemWindows(this, true)
        WindowInsetsControllerCompat(this, binding.root).show(WindowInsetsCompat.Type.systemBars())
    }

    private fun setAnimation() = with(binding) {
        loading.setAnimation("rico_loading_light_white.json")
    }

    fun displayLoading(isLoading: Boolean, manager: FragmentManager) {
        if (isLoading) {
            show(manager)
        } else {
            hide()
        }
    }

    private fun show(manager: FragmentManager) {
        if (!isShowing && !manager.isStateSaved) {
            isShowing = true
            show(manager, DIALOG_TAG)
        }
    }

    private fun hide() {
        if (isShowing && isVisible && !isStateSaved) {
            isShowing = false
            dismiss()
        }
    }

    fun forceHide() {
        if (isShowing) {
            isShowing = false
            dismiss()
        }
    }

    companion object {
        private const val DIALOG_TAG = "LOADING"

        fun newInstance(canBeCancelable: Boolean = false): DialogLoading {
            return DialogLoading(canBeCancelable)
        }
    }

}