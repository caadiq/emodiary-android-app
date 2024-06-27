package com.toy.project.emodiary.view.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.toy.project.emodiary.databinding.ActivitySignupBinding
import com.toy.project.emodiary.model.dto.SignUpDto
import com.toy.project.emodiary.model.utils.ResultUtil
import com.toy.project.emodiary.view.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySignupBinding.inflate(layoutInflater) }

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar()
        setupView()
        setupViewModel()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupView() {
        binding.btnSignUp.setOnClickListener {
            if (binding.editNickname.text.toString().isBlank()) {
                Toast.makeText(this, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.editEmail.text.toString().isBlank()) {
                Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.editPassword.text.toString().isBlank()) {
                Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.editPassword.text.toString() != binding.editPasswordConfirm.text.toString()) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.progressIndicator.visibility = View.VISIBLE
            signUp(binding.editNickname.text.toString(), binding.editEmail.text.toString(), binding.editPassword.text.toString())
        }
    }

    private fun setupViewModel() {
        authViewModel.apply {
            signUp.observe(this@SignUpActivity) { result ->
                when (result) {
                    is ResultUtil.Success -> {
                        Toast.makeText(this@SignUpActivity, result.data.message, Toast.LENGTH_SHORT).show()
                        val intent = Intent()
                        intent.putExtra("email", binding.editEmail.text.toString())
                        intent.putExtra("password", binding.editPasswordConfirm.text.toString())
                        setResult(RESULT_OK, intent).also { finish() }
                    }
                    is ResultUtil.Error -> {
                        Toast.makeText(this@SignUpActivity, result.error, Toast.LENGTH_SHORT).show()
                        binding.progressIndicator.visibility = View.GONE
                    }
                    is ResultUtil.NetworkError -> {
                        Toast.makeText(this@SignUpActivity, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                        binding.progressIndicator.visibility = View.GONE
                    }
                }
            }

            errorMessage.observe(this@SignUpActivity) { event ->
                event.getContentIfNotHandled()?.let { message ->
                    Toast.makeText(this@SignUpActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun signUp(nickname: String, email: String, password: String) {
        authViewModel.signUp(SignUpDto(email, password, nickname))
        binding.progressIndicator.show()
    }
}