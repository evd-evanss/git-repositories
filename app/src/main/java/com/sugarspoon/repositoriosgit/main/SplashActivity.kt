package com.sugarspoon.repositoriosgit.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.sugarspoon.core.base.BaseActivity
import com.sugarspoon.github.ui.git.GitHubActivity
import com.sugarspoon.repositoriosgit.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        delayInSplash()
    }

    private fun delayInSplash() {
        flowOf(3).onEach {
            delay(1000)
        }.onCompletion {
            withContext(Main) {
                startActivity(Intent(this@SplashActivity, GitHubActivity::class.java))
            }
        }.launchIn(CoroutineScope(IO))
    }

}