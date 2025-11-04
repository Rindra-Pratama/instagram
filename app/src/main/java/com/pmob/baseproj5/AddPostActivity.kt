package com.pmob.baseproj5

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pmob.baseproj5.databinding.ActivityAddPostBinding

class AddPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPostBinding
    private lateinit var appExecutors: AppExecutor
    private lateinit var postDao: PostDao
    private var imageUriString: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appExecutors = AppExecutor()
        postDao = AppDatabase.getDatabase(applicationContext).postDao()

        imageUriString = intent.getStringExtra("image_uri")

        imageUriString?.let {
            binding.ivSelectedImage.setImageURI(Uri.parse(it))
            // Sembunyikan tombol 'Tambah Gambar' karena sudah dipilih dari main
            binding.btnTambagambar.visibility = android.view.View.GONE
        }

        binding.btnSimpan.setOnClickListener {
            savePost()
        }
    }

    private fun savePost() {
        val username = binding.etUsername.text.toString().trim()
        val caption = binding.etCaption.text.toString().trim()

        if (username.isBlank() || caption.isBlank() || imageUriString.isNullOrBlank()) {
            Toast.makeText(this, "Isi semua kolom dulu!", Toast.LENGTH_LONG).show()
            return
        }

        appExecutors.diskIO.execute {
            val newPost = Post(
                username = username,
                caption = caption,
                imageUri = imageUriString!!
            )
            postDao.insert(newPost)
            appExecutors.mainThread.execute {
                Toast.makeText(this@AddPostActivity, "✅ Postingan berhasil dibuat!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}