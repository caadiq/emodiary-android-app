package com.toy.project.emodiary.view.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.toy.project.emodiary.model.data.UserData
import com.toy.project.emodiary.view.viewmodel.AuthViewModel
import com.toy.project.emodiary.view.viewmodel.DataStoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val dataStoreViewModel: DataStoreViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { true }

        setupViewModel()
    }

    private fun setupViewModel() {
        dataStoreViewModel.accessToken.observe(this) { accessToken ->
            if (accessToken != null)
                authViewModel.getUserInfo()
            else
                startActivity(Intent(this, SignInActivity::class.java)).also { finish() }
        }

        authViewModel.apply {
            userInfo.observe(this@SplashActivity) { user ->
                lifecycleScope.launch {
                    UserData.setUserData(user.email, user.nickname)
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java)).also { finish() }
                }
            }

            errorMessage.observe(this@SplashActivity) {
                startActivity(Intent(this@SplashActivity, SignInActivity::class.java)).also { finish() }
            }
        }
    }
}