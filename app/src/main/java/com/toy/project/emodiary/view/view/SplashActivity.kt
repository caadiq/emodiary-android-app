package com.toy.project.emodiary.view.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.toy.project.emodiary.databinding.ActivitySplashBinding
import com.toy.project.emodiary.model.data.UserData
import com.toy.project.emodiary.view.viewmodel.AuthViewModel
import com.toy.project.emodiary.view.viewmodel.DataStoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }

    private val dataStoreViewModel: DataStoreViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    private var isNavigated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupViewModel()
    }

    private fun setupViewModel() {
        dataStoreViewModel.accessToken.observe(this) { accessToken ->
            if (!isNavigated) {
                if (accessToken != null) {
                    authViewModel.getUserInfo()
                } else {
                    navigateToSignIn()
                }
            }
        }

        authViewModel.apply {
            userInfo.observe(this@SplashActivity) { user ->
                if (!isNavigated) {
                    lifecycleScope.launch {
                        UserData.setUserData(user.email, user.nickname)
                        navigateToMain()
                    }
                }
            }

            errorMessage.observe(this@SplashActivity) {
                if (!isNavigated) {
                    navigateToSignIn()
                }
            }
        }
    }

    private fun navigateToMain() {
        isNavigated = true
        startActivity(Intent(this, MainActivity::class.java)).also { finish() }
    }

    private fun navigateToSignIn() {
        isNavigated = true
        startActivity(Intent(this, SignInActivity::class.java)).also { finish() }
    }
}