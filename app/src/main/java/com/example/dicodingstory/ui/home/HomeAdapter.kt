package com.example.dicodingstory.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingstory.R
import com.example.dicodingstory.data.model.StoryModel

class HomeAdapter(private val listStory: List<StoryModel?>) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: StoryModel)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row_story, viewGroup, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = listStory[position]
        holder.apply {
            Glide.with(itemView.context)
                .load(story?.photoUrl)
                .centerCrop()
                .into(ivStory)
            tvName.text = story?.name
            tvDesc.text = story?.description
            tvDate.text = story?.createdAt
            itemView.setOnClickListener {
                if (story != null) {
                    onItemClickCallback.onItemClicked(story)
                }
            }
        }
    }

    override fun getItemCount() = listStory.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivStory: ImageView = view.findViewById(R.id.iv_story)
        val tvName: TextView = view.findViewById(R.id.tv_username)
        val tvDesc: TextView = view.findViewById(R.id.tv_description)
        val tvDate: TextView = view.findViewById(R.id.tv_date)
    }
}