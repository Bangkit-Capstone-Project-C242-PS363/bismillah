package com.adira.signmaster.ui.home.subscription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adira.signmaster.databinding.FragmentSubscriptionRequiredBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SubscriptionRequiredFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentSubscriptionRequiredBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubscriptionRequiredBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Close the fragment when the close button is clicked
        binding.btnClose.setOnClickListener {
            dismiss() // Closes the bottom sheet fragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Set the binding to null to prevent memory leaks
    }
}
