package com.example.dicodingstory.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingstory.data.model.StoryModel
import com.example.dicodingstory.databinding.ItemRowStoryBinding

@ExperimentalPagingApi
class StoryAdapter :
    PagingDataAdapter<StoryModel, StoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: StoryModel, optionsCompat: ActivityOptionsCompat)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRowStoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = getItem(position)
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

    inner class ViewHolder(binding: ItemRowStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val ivStory = binding.ivStory
        val tvName = binding.tvItemName
        val tvDesc = binding.tvItemDescription
        val tvDate = binding.tvItemDate
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryModel>() {
            override fun areItemsTheSame(oldItem: StoryModel, newItem: StoryModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: StoryModel,
                newItem: StoryModel
            ): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }
}