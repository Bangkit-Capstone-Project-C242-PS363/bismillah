package com.adira.signmaster.ui.study

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adira.signmaster.databinding.CardLearnMaterialBinding

data class LearnMaterialItem(
    val title: String,
    val imageResource: Int
)

class LearnMaterialAdapter(
    private val learnMaterialItems: List<LearnMaterialItem>,
    private val onItemClick: (LearnMaterialItem) -> Unit
) : RecyclerView.Adapter<LearnMaterialAdapter.LearnMaterialViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LearnMaterialViewHolder {
        val binding = CardLearnMaterialBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LearnMaterialViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LearnMaterialViewHolder, position: Int) {
        val learnMaterialItem = learnMaterialItems[position]
        holder.bind(learnMaterialItem)
    }

    override fun getItemCount(): Int = learnMaterialItems.size

    inner class LearnMaterialViewHolder(private val binding: CardLearnMaterialBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(learnMaterialItem: LearnMaterialItem) {
            binding.tvMaterialTitle.text = learnMaterialItem.title
            binding.iconStudyCard.setImageResource(learnMaterialItem.imageResource)
            binding.root.setOnClickListener {
                onItemClick(learnMaterialItem)
            }
        }
    }
}

