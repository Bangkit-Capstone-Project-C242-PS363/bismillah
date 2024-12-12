package com.adira.signmaster.ui.profile.history_study

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.adira.signmaster.R
import com.adira.signmaster.data.room.entity.StudyEntity
import com.adira.signmaster.databinding.CardLearnHistoryBinding
import com.bumptech.glide.Glide

class StudyHistoryAdapter : ListAdapter<StudyEntity, StudyHistoryAdapter.StudyHistoryViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudyHistoryViewHolder {
        val binding = CardLearnHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StudyHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudyHistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class StudyHistoryViewHolder(private val binding: CardLearnHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(study: StudyEntity) {
            binding.tvMaterialTitle.text = study.title
            Glide.with(binding.iconStudyCard.context)
                .load(study.icon_url)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_icon)
                .into(binding.iconStudyCard)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<StudyEntity>() {
        override fun areItemsTheSame(oldItem: StudyEntity, newItem: StudyEntity) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: StudyEntity, newItem: StudyEntity) = oldItem == newItem
    }
}