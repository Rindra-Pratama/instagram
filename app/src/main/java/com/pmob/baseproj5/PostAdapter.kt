package com.pmob.baseproj5

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pmob.baseproj5.databinding.ItemPostBinding

class PostAdapter (private val postList: List<Post>):
    RecyclerView.Adapter<PostAdapter.PostViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(
            LayoutInflater
                .from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PostViewHolder,
        position: Int
    ) {
        val post = postList[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    class PostViewHolder(private val binding: ItemPostBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post){
            binding.apply {
                tvUsername.text = post.username
                tvCaption.text = post.caption

                // Load image dari URI. Perlu izin READ_URI_PERMISSION.
                // Jika menggunakan library Coil, Anda bisa menggunakan binding.ivPostImage.load(Uri.parse(post.imageUri))
                // Karena kita tidak yakin Anda menginstall Coil, kita pakai setImageURI standar.
                ivPostImage.setImageURI(Uri.parse(post.imageUri))

                // Listener untuk tombol menu/edit/delete
                btnMenu.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra("post_id", post.id)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}