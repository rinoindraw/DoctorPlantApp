package com.rinoindraw.capstonerino.ui.history.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.rinoindraw.capstonerino.database.model.History
import com.rinoindraw.capstonerino.databinding.ItemHistoryRowBinding
import com.rinoindraw.capstonerino.ui.history.detail.DetailHistoryActivity
import com.rinoindraw.capstonerino.utils.setImageFromUrl

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private val stories: MutableList<History> = mutableListOf()

    class ViewHolder(private val binding: ItemHistoryRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(story: History) {
            binding.apply {
                tvHistoryTitle.text = story.penyakit
                tvHistoryDescription.text = story.deskripsi
                ivHistoryImage.setImageFromUrl(itemView.context, story.image)

                itemView.setOnClickListener {
                    val historyListJson = Gson().toJson(listOf(story))
                    val intent = Intent(itemView.context, DetailHistoryActivity::class.java)
                    intent.putExtra(DetailHistoryActivity.EXTRA_DETAIL_DATA, historyListJson)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemHistoryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = stories[position]
        holder.bind(story)
    }

    override fun getItemCount(): Int {
        return stories.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setStories(newStories: List<History>) {
        stories.clear()
        stories.addAll(newStories.sortedBy { it.penyakit })
        notifyDataSetChanged()
    }
}