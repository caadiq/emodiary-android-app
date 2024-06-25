package com.toy.project.emodiary.view.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.toy.project.emodiary.databinding.ActivitySigninBinding
import com.toy.project.emodiary.view.viewmodel.DataStoreViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySigninBinding.inflate(layoutInflater) }

    private val dataStoreViewModel: DataStoreViewModel by viewModels()

    private val startForRegisterResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            startActivity(Intent(this, MainActivity::class.java)).also { finish() }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
        setupViewModel()
    }

    private fun setupView() {
        binding.btnSignIn.setOnClickListener {
            dataStoreViewModel.setSaveId(binding.chkSaveId.isChecked)

            if (binding.chkSaveId.isChecked) {
                dataStoreViewModel.setEmail(binding.editEmail.text.toString())
            } else {
                dataStoreViewModel.deleteEmail()
            }

            startActivity(Intent(this, MainActivity::class.java)).also { finish() }
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
    }
}