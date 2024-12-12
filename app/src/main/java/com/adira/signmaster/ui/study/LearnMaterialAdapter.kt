package com.adira.signmaster.ui.study

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.adira.signmaster.R
import com.adira.signmaster.data.response.Chapter
import com.adira.signmaster.data.room.dao.StudyDao
import com.adira.signmaster.data.room.entity.StudyEntity
import com.adira.signmaster.databinding.CardLearnMaterialBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LearnMaterialAdapter(
    private val onItemClick: (Chapter) -> Unit,
    private val studyDao: StudyDao
) : ListAdapter<Chapter, LearnMaterialAdapter.LearnMaterialViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LearnMaterialViewHolder {
        val binding = CardLearnMaterialBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LearnMaterialViewHolder(binding, onItemClick, studyDao)
    }

    override fun onBindViewHolder(holder: LearnMaterialViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class LearnMaterialViewHolder(
        private val binding: CardLearnMaterialBinding,
        private val onItemClick: (Chapter) -> Unit,
        private val studyDao: StudyDao
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(chapter: Chapter) {
            binding.tvMaterialTitle.text = chapter.title
            Glide.with(binding.iconStudyCard.context)
                .load(chapter.icon_url)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_icon)
                .into(binding.iconStudyCard)

            CoroutineScope(Dispatchers.IO).launch {
                val isSaved = studyDao.getStudyById(chapter.id) != null
                withContext(Dispatchers.Main) {
                    updateBookmarkIcon(isSaved)
                }
            }

            itemView.setOnClickListener {
                val animation = AnimationUtils.loadAnimation(itemView.context, R.anim.scale_animation)
                itemView.startAnimation(animation)
                onItemClick(chapter)
            }

            binding.ivSaveMaterial.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val isSaved = studyDao.getStudyById(chapter.id) != null
                    if (isSaved) {
                        studyDao.deleteStudy(StudyEntity(
                            id = chapter.id,
                            title = chapter.title,
                            icon_url = chapter.icon_url,
                            saved = true
                        ))
                        withContext(Dispatchers.Main) {
                            updateBookmarkIcon(false)
                            Toast.makeText(itemView.context, "Unmarked successfully!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        studyDao.insertStudy(
                            StudyEntity(
                                id = chapter.id,
                                title = chapter.title,
                                icon_url = chapter.icon_url,
                                saved = true
                            )
                        )
                        withContext(Dispatchers.Main) {
                            updateBookmarkIcon(true)
                            Toast.makeText(itemView.context, "Marked successfully!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        private fun updateBookmarkIcon(isSaved: Boolean) {
            binding.ivSaveMaterial.setImageResource(
                if (isSaved) R.drawable.baseline_bookmark_24 else R.drawable.baseline_bookmark_border
            )
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Chapter>() {
        override fun areItemsTheSame(oldItem: Chapter, newItem: Chapter) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Chapter, newItem: Chapter) = oldItem == newItem
    }
}











