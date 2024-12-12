package com.adira.signmaster.ui.study.material_list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.adira.signmaster.R
import com.adira.signmaster.data.response.MaterialDetail
import com.adira.signmaster.databinding.MaterialListCardBinding

class MaterialListAdapter(
    private val onItemClick: (MaterialDetail) -> Unit
) : ListAdapter<MaterialDetail, MaterialListAdapter.MaterialViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialViewHolder {
        val binding = MaterialListCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MaterialViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: MaterialViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MaterialViewHolder(
        private val binding: MaterialListCardBinding,
        private val onItemClick: (MaterialDetail) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(material: MaterialDetail) {
            binding.tvMaterialTitle.text = material.title
            itemView.setOnClickListener {
                val animation = AnimationUtils.loadAnimation(itemView.context, R.anim.scale_animation)
                itemView.startAnimation(animation)
                onItemClick(material)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MaterialDetail>() {
        override fun areItemsTheSame(oldItem: MaterialDetail, newItem: MaterialDetail) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: MaterialDetail, newItem: MaterialDetail) = oldItem == newItem
    }
}


