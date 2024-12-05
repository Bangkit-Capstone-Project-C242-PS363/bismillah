package com.adira.signmaster.ui.study

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.adira.signmaster.data.response.Chapter
import com.adira.signmaster.databinding.CardLearnMaterialBinding
import com.bumptech.glide.Glide

class LearnMaterialAdapter(
    private val onItemClick: (Chapter) -> Unit
) : ListAdapter<Chapter, LearnMaterialAdapter.LearnMaterialViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LearnMaterialViewHolder {
        val binding = CardLearnMaterialBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LearnMaterialViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: LearnMaterialViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class LearnMaterialViewHolder(
        private val binding: CardLearnMaterialBinding,
        private val onItemClick: (Chapter) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(chapter: Chapter) {
            binding.tvMaterialTitle.text = chapter.title
            Glide.with(binding.iconStudyCard.context)
                .load(chapter.icon_url)
                .into(binding.iconStudyCard)

            binding.root.setOnClickListener {
                onItemClick(chapter)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Chapter>() {
        override fun areItemsTheSame(oldItem: Chapter, newItem: Chapter) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Chapter, newItem: Chapter) = oldItem == newItem
    }
}



