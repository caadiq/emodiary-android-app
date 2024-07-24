package com.toy.project.emodiary.view.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.toy.project.emodiary.databinding.FragmentProfileBinding
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupViewModel() {
        diaryViewModel.apply {
            getMyInformation()

            myInfo.observe(viewLifecycleOwner) {
                binding.txtNickname.text = it.nickname
                binding.txtFirstWritten.text = if (it.firstDiaryDate == null) {
                    "작성한 일기가 없습니다."
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
    }
}