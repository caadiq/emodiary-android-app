package com.toy.project.emodiary.view.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.toy.project.emodiary.databinding.DialogCustomBinding

class CustomDialog(private val title: String?, private val message: String, private val onConfirm: () -> Unit) : DialogFragment() {
    private var _binding: DialogCustomBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = DialogCustomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDialog()
        setupView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupDialog() {
        dialog?.apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.attributes?.width = (context.resources.displayMetrics.widthPixels.times(0.8)).toInt()
        }
    }

    private fun setupView() {
        binding.txtTitle.apply {
            visibility = if (title.isNullOrEmpty()) View.GONE else View.VISIBLE
            text = title
        }

        binding.txtMessage.text = message

        binding.txtCancel.setOnClickListener {
            dismiss()
        }

        binding.txtConrifm.setOnClickListener {
            onConfirm()
            dismiss()
        }
    }
}