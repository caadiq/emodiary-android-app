package com.toy.project.emodiary.view.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import com.bumptech.glide.Glide
import com.toy.project.emodiary.R
import com.toy.project.emodiary.databinding.ActivityDiaryBinding
import com.toy.project.emodiary.view.utils.DateTimeConverter.stringToDate
import com.toy.project.emodiary.view.viewmodel.DiaryViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.format.DateTimeFormatter
import java.util.Locale

@AndroidEntryPoint
class DiaryActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDiaryBinding.inflate(layoutInflater) }

    private val diaryViewModel: DiaryViewModel by viewModels()

    private lateinit var customDialog: CustomDialog
    private lateinit var progressDialog: ProgressDialog

    private val date by lazy { intent.getStringExtra("date") }
    private val diaryId by lazy { intent.getIntExtra("diaryId", -1) }
    private val title by lazy { intent.getStringExtra("title") }
    private val content by lazy { intent.getStringExtra("content") }
    private val emotion by lazy { intent.getStringExtra("emotion") }
    private val weather by lazy { intent.getStringExtra("weather") }
    private val wordCloud by lazy { intent.getStringExtra("wordCloud") }

    private val startForRegisterResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar()
        setupView()
        setupViewModel()
        setupDialog()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_diary, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.edit -> {
                        val intent = Intent(this@DiaryActivity, DiaryEditActivity::class.java)
                        intent.putExtra("toolbarTitle", "일기 수정")
                        intent.putExtra("diaryId", diaryId)
                        intent.putExtra("date", date)
                        intent.putExtra("title", title)
                        intent.putExtra("content", content)
                        startForRegisterResult.launch(intent)
                    }
                    R.id.delete -> {
                        if (diaryId != -1)
                            customDialog.show(supportFragmentManager, "CustomDialog")
                    }
                }
                return false
            }
        }, this)
    }

    private fun setupView() {
        val localDate = date?.let { stringToDate(it, "yyyy-MM-dd", Locale.KOREA) }
        binding.txtDate.text = localDate?.format(DateTimeFormatter.ofPattern("yyyy년 M월 d일 EEEE", Locale.KOREA))
        binding.txtTitle.text = title
        binding.txtContent.text = content

        binding.divider.visibility = if (wordCloud.isNullOrEmpty()) View.GONE else View.VISIBLE
        binding.txtWordCloud.visibility = if (wordCloud.isNullOrEmpty()) View.GONE else View.VISIBLE
        Glide.with(this).load(emotion).into(binding.imgMood)
        Glide.with(this).load(weather).into(binding.imgWeather)
        Glide.with(this).load(wordCloud).into(binding.imgWordCloud)
    }

    private fun setupViewModel() {
        diaryViewModel.apply {
            deleteDiary.observe(this@DiaryActivity) {
                progressDialog.dismiss()
                finish()
            }

            errorMessage.observe(this@DiaryActivity) { event ->
                progressDialog.dismiss()
                event.getContentIfNotHandled()?.let { message ->
                    if (!message.lowercase().contains("jwt"))
                        Toast.makeText(this@DiaryActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupDialog() {
        customDialog = CustomDialog(
            title = null,
            message = "삭제하시겠습니까?",
            onConfirm = {
                diaryViewModel.deleteDiary(diaryId)
                progressDialog.show(supportFragmentManager, "ProgressDialog")
            }
        )

        progressDialog = ProgressDialog("삭제 중입니다...")
    }
}