package com.toy.project.emodiary.view.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import com.bumptech.glide.Glide
import com.toy.project.emodiary.R
import com.toy.project.emodiary.databinding.ActivityDiaryBinding
import com.toy.project.emodiary.view.utils.DateTimeConverter.stringToDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class DiaryActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDiaryBinding.inflate(layoutInflater) }

    private val date by lazy { intent.getStringExtra("date") }
    private val title by lazy { intent.getStringExtra("title") }
    private val content by lazy { intent.getStringExtra("content") }
    private val wordCloud by lazy { intent.getStringExtra("wordCloud") }

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

        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_diary, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.edit) {
                    val intent = Intent(this@DiaryActivity, DiaryEditActivity::class.java)
                    intent.putExtra("toolbarTitle", "일기 수정")
                    intent.putExtra("date", date)
                    intent.putExtra("title", title)
                    intent.putExtra("content", content)
                    startActivity(intent)
                    return true
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
        Glide.with(this).load(wordCloud).into(binding.imgWordCloud)
    }
}