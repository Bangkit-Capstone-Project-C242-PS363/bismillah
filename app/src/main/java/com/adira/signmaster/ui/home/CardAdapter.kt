package com.adira.signmaster.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adira.signmaster.R

class CardAdapter(private val items: List<CardItem>) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.tv_event_name)
        val imageThumbnail: ImageView = itemView.findViewById(R.id.img_Logo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_news, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = items[position]
        holder.titleText.text = item.title
        holder.imageThumbnail.setImageResource(item.imageResId)
    }

    override fun getItemCount() = items.size
}

data class CardItem(val title: String, val imageResId: Int)