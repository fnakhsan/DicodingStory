package com.example.dicodingstory.ui.home

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingstory.R
import com.example.dicodingstory.data.model.StoryModel

class HomeAdapter(private val listStory: List<StoryModel?>) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: StoryModel, optionsCompat: ActivityOptionsCompat)
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
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            androidx.core.util.Pair(ivStory, "image"),
                            androidx.core.util.Pair(tvName, "name"),
                            androidx.core.util.Pair(tvDesc, "description"),
                            androidx.core.util.Pair(tvDate, "date")
                        )
                    onItemClickCallback.onItemClicked(story, optionsCompat)
                }
            }
        }
    }

    override fun getItemCount() = listStory.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivStory: ImageView = view.findViewById(R.id.iv_story)
        val tvName: TextView = view.findViewById(R.id.tv_item_name)
        val tvDesc: TextView = view.findViewById(R.id.tv_item_description)
        val tvDate: TextView = view.findViewById(R.id.tv_item_date)
    }
}