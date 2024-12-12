package com.adira.signmaster.ui.quiz.Sertifikat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adira.signmaster.databinding.FragmentCompletionRequiredBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CompletionRequiredFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentCompletionRequiredBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompletionRequiredBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Close button listener
        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): CompletionRequiredFragment {
            return CompletionRequiredFragment()
        }
    }
}
