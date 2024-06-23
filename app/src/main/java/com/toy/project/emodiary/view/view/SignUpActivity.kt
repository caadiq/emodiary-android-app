package com.toy.project.emodiary.view.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.toy.project.emodiary.databinding.ActivitySignupBinding

class SignUpActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySignupBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar()
        setupView()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupView() {
        binding.btnSignUp.setOnClickListener {
            setResult(RESULT_OK).also { finish() }
        }
    }
}