package com.toy.project.emodiary.view.view

//import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.toy.project.emodiary.databinding.ActivitySplashBinding
import com.toy.project.emodiary.model.data.UserData
import com.toy.project.emodiary.view.utils.RequestPermissionUtil
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

    private val requestPermissionUtil by lazy { RequestPermissionUtil(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (requestPermissionUtil.isLocationPermissionGranted()) {
            setupViewModel()
        } else {
            requestPermissionUtil.requestLocation()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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