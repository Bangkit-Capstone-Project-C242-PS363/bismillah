package com.adira.signmaster.ui.quiz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adira.signmaster.R
import com.adira.signmaster.data.model.Chapter
import com.bumptech.glide.Glide

class ChapterAdapter(private val onClick: (Int, String) -> Unit) :
RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder>() {

    private val chapters = mutableListOf<Chapter>()

    fun submitList(newChapters: List<Chapter>) {
        chapters.clear()
        chapters.addAll(newChapters)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_quiz, parent, false)
        return ChapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {
        val chapter = chapters[position]
        holder.bind(chapter)
        holder.itemView.setOnClickListener { onClick(chapter.id, chapter.title) } // Pass both id and title
    }

    override fun getItemCount() = chapters.size

    class ChapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.tvQuizTitle)
        private val icon: ImageView = itemView.findViewById(R.id.ivIconQuizCard)

        fun bind(chapter: Chapter) {
            title.text = chapter.title
            Glide.with(itemView.context)
                .load(chapter.icon_url)
                .placeholder(R.drawable.default_icon)
                .error(R.drawable.error_icon)
                .into(icon)
        }
    }
}
