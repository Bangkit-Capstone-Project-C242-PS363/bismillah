package com.adira.signmaster.ui.study.material_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.adira.signmaster.data.response.MaterialDetail
import com.adira.signmaster.databinding.MaterialListCardBinding

class MaterialListAdapter(private val onItemClick: (MaterialDetail) -> Unit) : ListAdapter<MaterialDetail, MaterialListAdapter.MaterialViewHolder>(MaterialDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialViewHolder {
        val binding = MaterialListCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MaterialViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MaterialViewHolder, position: Int) {
        val material = getItem(position)
        holder.bind(material, position)
    }

    inner class MaterialViewHolder(private val binding: MaterialListCardBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val material = getItem(adapterPosition)
                onItemClick(material)
            }
        }

        fun bind(material: MaterialDetail, position: Int) {
            binding.tvMaterialTitle.text = material.title
        }
    }

    class MaterialDiffCallback : DiffUtil.ItemCallback<MaterialDetail>() {
        override fun areItemsTheSame(oldItem: MaterialDetail, newItem: MaterialDetail): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MaterialDetail, newItem: MaterialDetail): Boolean {
            return oldItem == newItem
        }
    }
}



