package com.toy.project.emodiary.view.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.toy.project.emodiary.databinding.ActivitySigninBinding
import com.toy.project.emodiary.model.data.UserData
import com.toy.project.emodiary.model.dto.SignInDto
import com.toy.project.emodiary.view.viewmodel.AuthViewModel
import com.toy.project.emodiary.view.viewmodel.DataStoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySigninBinding.inflate(layoutInflater) }

    private val dataStoreViewModel: DataStoreViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    private lateinit var progressDialog: ProgressDialog

    private val startForRegisterResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val email = data?.getStringExtra("email") ?: ""
            val password = data?.getStringExtra("password") ?: ""
            signIn(email, password)
        }
    }

    private var backPressedTime: Long = 0
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() - backPressedTime >= 2000) {
                backPressedTime = System.currentTimeMillis()
                Toast.makeText(this@SignInActivity, "뒤로 가기 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            } else {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        setupView()
        setupViewModel()
        setupDialog()
    }

    private fun setupView() {
        binding.btnSignIn.setOnClickListener {
            if (binding.editEmail.text.toString().isBlank()) {
                Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.editPassword.text.toString().isBlank()) {
                Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            signIn(binding.editEmail.text.toString(), binding.editPassword.text.toString())
        }

        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startForRegisterResult.launch(intent)
        }
    }

    private fun setupViewModel() {
        dataStoreViewModel.apply {
            saveId.observe(this@SignInActivity) { isSaveId ->
                if (isSaveId == null) {
                    setSaveId(true)
                    binding.chkSaveId.isChecked = true
                } else {
                    binding.chkSaveId.isChecked = isSaveId
                }
            }

            email.observe(this@SignInActivity) { email ->
                email?.let { binding.editEmail.setText(it) }
            }
        }

        authViewModel.apply {
            signIn.observe(this@SignInActivity) {
                lifecycleScope.launch {
                    dataStoreViewModel.setSaveId(binding.chkSaveId.isChecked)

                    if (binding.chkSaveId.isChecked)
                        dataStoreViewModel.setEmail(binding.editEmail.text.toString())
                    else
                        dataStoreViewModel.deleteEmail()

                    it.token.accessToken?.let { token -> dataStoreViewModel.setAccessToken(token) }
                    it.token.refreshToken?.let { token -> dataStoreViewModel.setRefreshToken(token) }
                    UserData.setUserData(it.user.email, it.user.nickname)

                    progressDialog.dismiss()

                    startActivity(Intent(this@SignInActivity, MainActivity::class.java)).also { finish() }
                }
            }

            errorMessage.observe(this@SignInActivity) { event ->
                progressDialog.dismiss()
                event.getContentIfNotHandled()?.let { message ->
                    if (!message.lowercase().contains("jwt"))
                        Toast.makeText(this@SignInActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupDialog() {
        progressDialog = ProgressDialog("로그인 중입니다")
    }

    private fun signIn(email: String, password: String) {
        authViewModel.signIn(SignInDto(email, password))
        progressDialog.show(supportFragmentManager, "ProgressDialog")
    }
}