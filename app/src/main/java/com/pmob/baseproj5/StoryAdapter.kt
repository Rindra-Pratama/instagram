package com.pmob.baseproj5

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pmob.baseproj5.databinding.ItemStoryBinding

class StoryAdapter(private val usernames: List<String>):
    RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(usernames[position])
    }

    override fun getItemCount(): Int {
        return usernames.size
    }

    class StoryViewHolder(private val binding: ItemStoryBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(username: String) {
            binding.tvStoryUsername.text = username
            // Untuk gambar, kita bisa set ImageView secara statis di item_story.xml
            // atau menggunakan library seperti Coil jika Anda memiliki Drawable ID.
        }
    }
}