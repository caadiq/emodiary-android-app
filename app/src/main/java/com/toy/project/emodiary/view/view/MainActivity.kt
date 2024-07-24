package com.toy.project.emodiary.view.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.toy.project.emodiary.R
import com.toy.project.emodiary.databinding.ActivityMainBinding
import com.toy.project.emodiary.view.utils.RequestPermissionUtil
import com.toy.project.emodiary.view.viewmodel.DiaryViewModel
import com.toy.project.emodiary.view.viewmodel.MainFragmentType
import com.toy.project.emodiary.view.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val mainViewModel: MainViewModel by viewModels()
    private val diaryViewModel: DiaryViewModel by viewModels()

    private val requestPermissionUtil by lazy { RequestPermissionUtil(this) }

    private var isWritten: Boolean? = null

    private var backPressedTime: Long = 0
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() - backPressedTime >= 2000) {
                backPressedTime = System.currentTimeMillis()
                Toast.makeText(this@MainActivity, "뒤로 가기 버튼을 한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            } else {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        if (!requestPermissionUtil.isLocationPermissionGranted()) {
            requestPermissionUtil.requestLocation()
        }

        if (savedInstanceState == null) setupFragment()
        setupView()
        setupViewModel()
    }

    private fun setupFragment() {
        supportFragmentManager.beginTransaction().apply {
            add(binding.fragmentContainerView.id, HomeFragment(), MainFragmentType.HOME.tag)
            add(binding.fragmentContainerView.id, ProfileFragment(), MainFragmentType.PROFILE.tag)
            commit()
        }
    }

    private fun setupView() {
        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> mainViewModel.setCurrentFragment(0)
                R.id.profile -> mainViewModel.setCurrentFragment(1)
            }
            true
        }

        binding.fab.setOnClickListener {
            if (isWritten == true) {
                Toast.makeText(this, "오늘 일기를 이미 작성했습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val today = LocalDate.now()
            val intent = Intent(this, DiaryEditActivity::class.java)
            intent.putExtra("toolbarTitle", "일기 작성")
            intent.putExtra("date", today.toString())
            startActivity(intent)
        }
    }

    private fun setupViewModel() {
        mainViewModel.currentFragmentType.observe(this) { fragmentType ->
            val currentFragment = supportFragmentManager.findFragmentByTag(fragmentType.tag)
            supportFragmentManager.beginTransaction().apply {
                setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                supportFragmentManager.fragments.forEach { fragment ->
                    if (fragment == currentFragment)
                        show(fragment)
                    else
                        hide(fragment)
                }
            }.commit()

            binding.bottomNav.selectedItemId = when (fragmentType) {
                MainFragmentType.HOME -> R.id.home
                MainFragmentType.PROFILE -> R.id.profile
                else -> R.id.home
            }
        }

        diaryViewModel.isWritten.observe(this) {
            isWritten = it
        }
    }
}