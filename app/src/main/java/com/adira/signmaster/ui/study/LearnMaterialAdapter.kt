package com.adira.signmaster.ui.study

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adira.signmaster.data.response.Chapter
import com.adira.signmaster.databinding.CardLearnMaterialBinding
import com.bumptech.glide.Glide

class LearnMaterialAdapter(private val chapters: List<Chapter>) :
    RecyclerView.Adapter<LearnMaterialAdapter.LearnMaterialViewHolder>() {

    inner class LearnMaterialViewHolder(val binding: CardLearnMaterialBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LearnMaterialViewHolder {
        val binding = CardLearnMaterialBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LearnMaterialViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LearnMaterialViewHolder, position: Int) {
        val chapter = chapters[position]
        holder.binding.tvMaterialTitle.text = chapter.title

        // Memuat icon menggunakan Glide
        Glide.with(holder.itemView.context)
            .load(chapter.iconUrl)
            .into(holder.binding.iconStudyCard)

        // Menambahkan data ID ke dalam item (misalnya ditampilkan di log atau digunakan dalam navigasi)
        holder.itemView.setOnClickListener {
            // Handle item click
            println("Item clicked: ${chapter.id}")
        }
    }

    override fun getItemCount(): Int = chapters.size
}


