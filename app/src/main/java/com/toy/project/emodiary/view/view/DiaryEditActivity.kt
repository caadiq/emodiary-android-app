package com.toy.project.emodiary.view.view

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.LocationServices
import com.toy.project.emodiary.databinding.ActivityDiaryEditBinding
import com.toy.project.emodiary.model.dto.DiaryAddDto
import com.toy.project.emodiary.model.dto.DiaryEditDto
import com.toy.project.emodiary.view.utils.DateTimeConverter.stringToDate
import com.toy.project.emodiary.view.utils.KeyboardVisibilityUtils
import com.toy.project.emodiary.view.viewmodel.DiaryViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.format.DateTimeFormatter
import java.util.Locale

@AndroidEntryPoint
class DiaryEditActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDiaryEditBinding.inflate(layoutInflater) }

    private val diaryViewModel: DiaryViewModel by viewModels()

    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils

    private lateinit var customDialog: CustomDialog
    private lateinit var progressDialog: ProgressDialog

    private val toolbarTitle by lazy { intent.getStringExtra("toolbarTitle") }
    private val diaryId by lazy { intent.getIntExtra("diaryId", -1) }
    private val date by lazy { intent.getStringExtra("date") }
    private val title by lazy { intent.getStringExtra("title") }
    private val content by lazy { intent.getStringExtra("content") }

    private var latitude: Double? = null
    private var longitude: Double? = null

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
        setupViewModel()
        getLocation()
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

        binding.txtComplete.setOnClickListener {
            val title = binding.editTitle.text.toString()
            val content = binding.editContent.text.toString()

            if (title.isBlank()) {
                Toast.makeText(this, "제목을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (content.isBlank()) {
                Toast.makeText(this, "내용을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (toolbarTitle?.contains("작성") == true) {
                date?.let {
                    diaryViewModel.addDiary(DiaryAddDto(it, title, content, latitude, longitude))
                    progressDialog.show(supportFragmentManager, "ProgressDialog")
                }
            } else {
                if (diaryId != -1) {
                    diaryViewModel.editDiary(diaryId, DiaryEditDto(title, content))
                    progressDialog.show(supportFragmentManager, "ProgressDialog")
                }
            }
        }

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

        val message = if (toolbarTitle?.contains("작성") == true) "작성 중입니다" else "수정 중입니다"
        progressDialog = ProgressDialog(message)
    }

    private fun setupViewModel() {
        diaryViewModel.apply {
            addDiary.observe(this@DiaryEditActivity) {
                progressDialog.dismiss()
                finish()
            }

            editDiary.observe(this@DiaryEditActivity) {
                progressDialog.dismiss()
                setResult(RESULT_OK, intent).also { finish() }
            }

            errorMessage.observe(this@DiaryEditActivity) { event ->
                progressDialog.dismiss()
                event.getContentIfNotHandled()?.let { message ->
                    if (!message.lowercase().contains("jwt"))
                        Toast.makeText(this@DiaryEditActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
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

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { success: Location? ->
                success?.let { location ->
                    latitude = location.latitude
                    longitude = location.longitude
                }
            }
            .addOnFailureListener {}
    }
}