package com.toy.project.emodiary.view.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.toy.project.emodiary.databinding.FragmentProfileBinding
import com.toy.project.emodiary.model.data.UserData
import com.toy.project.emodiary.view.viewmodel.AuthViewModel
import com.toy.project.emodiary.view.viewmodel.DataStoreViewModel
import com.toy.project.emodiary.view.viewmodel.DiaryViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val diaryViewModel: DiaryViewModel by activityViewModels()
    private val authViewModel: AuthViewModel by viewModels()
    private val dataStoreViewModel: DataStoreViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding.btnSignOut.setOnClickListener {
            authViewModel.signOut()
        }
    }

    private fun setupViewModel() {
        diaryViewModel.apply {
            getMyInformation()

            myInfo.observe(viewLifecycleOwner) {
                binding.txtNickname.text = it.nickname
                binding.txtFirstWritten.text = if (it.firstDiaryDate == null) {
                    "첫 일기를 작성해보세요!"
                } else {
                    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.KOREA)
                    val outputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd (E)", Locale.KOREA)
                    val date = LocalDate.parse(it.firstDiaryDate, inputFormatter)
                    date.format(outputFormatter)
                }
                binding.txtProgress.text = "${it.percentage}%"
                binding.progressIndicator.progress = it.percentage.toFloat().toInt()
            }
        }

        authViewModel.apply {
            signOut.observe(viewLifecycleOwner) {
                dataStoreViewModel.deleteAccessToken()
                dataStoreViewModel.deleteRefreshToken()
                UserData.clearUserData()
                startActivity(Intent(requireContext(), SignInActivity::class.java)).also { activity?.finish() }
            }
        }
    }
}