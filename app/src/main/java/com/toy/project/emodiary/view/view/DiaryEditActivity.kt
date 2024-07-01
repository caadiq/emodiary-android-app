package com.toy.project.emodiary.view.view

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.toy.project.emodiary.databinding.ActivityDiaryEditBinding
import com.toy.project.emodiary.view.utils.DateTimeConverter.stringToDate
import com.toy.project.emodiary.view.utils.KeyboardVisibilityUtils
import java.time.format.DateTimeFormatter
import java.util.Locale


class DiaryEditActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDiaryEditBinding.inflate(layoutInflater) }

    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils

    private lateinit var customDialog: CustomDialog

    private val toolbarTitle by lazy { intent.getStringExtra("toolbarTitle") }
    private val date by lazy { intent.getStringExtra("date") }
    private val title by lazy { intent.getStringExtra("title") }
    private val content by lazy { intent.getStringExtra("content") }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            customDialog.show(supportFragmentManager, "CustomDialog")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        setupToolbar()
        setupView()
        setupDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        keyboardVisibilityUtils.detachKeyboardListeners()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { customDialog.show(supportFragmentManager, "CustmoDialog") }
        binding.txtTitle.text = toolbarTitle
    }

    private fun setupView() {
        val localDate = date?.let { stringToDate(it, "yyyy-MM-dd", Locale.KOREA) }
        binding.txtDate.text = localDate?.format(DateTimeFormatter.ofPattern("yyyy년 M월 d일 EEEE", Locale.KOREA))
        binding.editTitle.setText(title)
        binding.editContent.setText(content)

        keyboardVisibilityUtils = KeyboardVisibilityUtils(window,
            onShowKeyboard = { scrollToCursor() },
            onHideKeyboard = { scrollToCursor() }
        )
    }

    private fun setupDialog() {
        customDialog = CustomDialog(
            title = null,
            message = if (toolbarTitle?.contains("작성") == true) "작성을 취소하시겠습니까?" else "수정을 취소하시겠습니까?",
            onConfirm = { finish() }
        )
    }

    private fun scrollToCursor() {
        binding.editContent.post {
            val layout = binding.editContent.layout
            if (layout != null) {
                val line = layout.getLineForOffset(binding.editContent.selectionStart)
                val y = layout.getLineTop(line)
                binding.scrollView.smoothScrollTo(0, y)
            }
        }
    }
}