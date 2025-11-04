package com.pmob.baseproj5

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.pmob.baseproj5.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var appExecutors: AppExecutor
    private var currentPostId: Int = -1
    private var currentImageUri: String? = null

    // Launcher untuk memilih gambar baru saat mengedit
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            // Memberikan izin akses permanen ke URI
            contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            currentImageUri = it.toString()
            binding.ivSelectedImage.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appExecutors = AppExecutor()
        currentPostId = intent.getIntExtra("post_id", -1)

        if (currentPostId != -1) {
            loadPostData(currentPostId)
        } else {
            Toast.makeText(this, "Postingan tidak ditemukan.", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.btnTambagambar.setOnClickListener {
            galleryLauncher.launch("image/*") // Membuka galeri untuk memilih gambar baru
        }

        binding.btnUpdate.setOnClickListener {
            updatePost()
        }

        binding.btnDelete.setOnClickListener {
            deletePost()
        }
    }

    private fun loadPostData(postId: Int) {
        appExecutors.diskIO.execute {
            val dao = AppDatabase.getDatabase(this@DetailActivity).postDao()
            val selectedPost = dao.getPostById(postId)

            appExecutors.mainThread.execute {
                binding.apply {
                    etUsername.setText(selectedPost.username)
                    etCaption.setText(selectedPost.caption)

                    currentImageUri = selectedPost.imageUri
                    ivSelectedImage.setImageURI(Uri.parse(currentImageUri))
                }
            }
        }
    }

    private fun updatePost() {
        val newUsername = binding.etUsername.text.toString()
        val newCaption = binding.etCaption.text.toString()

        if (newUsername.isBlank() || newCaption.isBlank() || currentImageUri.isNullOrBlank()) {
            Toast.makeText(this, "Semua kolom harus diisi dan gambar dipilih", Toast.LENGTH_SHORT).show()
            return
        }

        appExecutors.diskIO.execute {
            val dao = AppDatabase.getDatabase(this@DetailActivity).postDao()
            val updatedPost = Post(
                id = currentPostId, // PENTING: ID harus sama
                username = newUsername,
                caption = newCaption,
                imageUri = currentImageUri!!
            )
            dao.update(updatedPost)
            appExecutors.mainThread.execute {
                Toast.makeText(this@DetailActivity, "✅ Postingan berhasil diperbarui", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun deletePost() {
        appExecutors.diskIO.execute {
            val dao = AppDatabase.getDatabase(this@DetailActivity).postDao()
            val postToDelete = dao.getPostById(currentPostId)
            dao.delete(postToDelete)
            appExecutors.mainThread.execute {
                Toast.makeText(this@DetailActivity, "🗑️ Postingan berhasil dihapus", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}