package com.pmob.baseproj5

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pmob.baseproj5.databinding.ActivityMainBinding
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: AppDatabase
    private lateinit var postDao: PostDao
    private lateinit var appExecutors: AppExecutor
    private val storyUsernames = listOf("intan_dwi", "minda_04", "rubi_community", "rizka", "amelia")

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            showAddPostScreen(it.toString())
        } ?: Toast.makeText(this, "Pemilihan gambar dibatalkan.", Toast.LENGTH_SHORT).show()
    }
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            galleryLauncher.launch("image/*")
        } else {
            Toast.makeText(this, "Izin akses galeri ditolak. Tidak bisa menambah postingan.", Toast.LENGTH_LONG).show()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appExecutors = AppExecutor()
        db = AppDatabase.getDatabase(applicationContext)
        postDao = db.postDao()
        setupStoryRecyclerView()
        setupFeedRecyclerView()
        binding.fabAdd.setOnClickListener {
            checkPermissionAndOpenGallery()
        }
    }

    private fun setupStoryRecyclerView() {
        val storyAdapter = StoryAdapter(storyUsernames)
        binding.rvStory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = storyAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupFeedRecyclerView() {
        val postListLiveData = postDao.getAllPost()
        postListLiveData.observe(this, Observer { list ->
            binding.rvRoomDb.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = PostAdapter(list)
            }
        })
    }

    private fun checkPermissionAndOpenGallery() {
        val permission = if (android.os.Build.VERSION.SDK_INT >= 33) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            galleryLauncher.launch("image/*")
        } else {
            requestPermissionLauncher.launch(permission)
        }
    }

    private fun showAddPostScreen(imageUri: String) {
        val intent = Intent(this, AddPostActivity::class.java)
        intent.putExtra("image_uri", imageUri)
        startActivity(intent)
    }
}
