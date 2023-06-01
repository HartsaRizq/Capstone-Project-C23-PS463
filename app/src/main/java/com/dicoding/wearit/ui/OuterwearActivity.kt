package com.dicoding.wearit.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.dicoding.wearit.createTempFile
import com.dicoding.wearit.databinding.ActivityOuterwearBinding
import java.io.File

class OuterwearActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOuterwearBinding
    private lateinit var currentPhotoPath: String
    private var currentPhotoId: Int = 0
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOuterwearBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val image1 = binding.iv1
        val image2 = binding.iv2
        val image3 = binding.iv3
        val image4 = binding.iv4
        val image5 = binding.iv5

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }


        setupImageViewClickListeners()
    }

    private fun setupImageViewClickListeners() {
        val imageViews = listOf(binding.iv1, binding.iv2, binding.iv3, binding.iv4, binding.iv5)
        imageViews.forEach { imageView ->
            imageView.setOnClickListener { openImageSelectionDialog(imageView) }
        }
    }

    private fun openImageSelectionDialog(imageView: ImageView) {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Choose an option")
        builder.setItems(options) { dialog, item ->
            when {
                options[item] == "Take Photo" -> startTakePhoto(imageView)
//                options[item] == "Choose from Gallery" -> openGallery(imageView)
                options[item] == "Cancel" -> dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun startTakePhoto(imageView: ImageView) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        val imageTag = imageView.id
        Log.d("Ayam", "Image Tag: $imageTag")
        createTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@OuterwearActivity,
                "com.dicoding.wearit",
                it
            )
            currentPhotoPath = it.absolutePath
            currentPhotoId = imageTag
            intent.putExtra("imageId", currentPhotoId)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(intent, REQUEST_CODE_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            val imageViewId = data?.getIntExtra("imageId", currentPhotoId)
            Log.d("Chicken", "ID: $imageViewId")
            val imageView = findImageViewById(imageViewId)
            imageView?.setImageBitmap(BitmapFactory.decodeFile(myFile.path))
        }
    }
    private fun findImageViewById(imageViewId: Int?): ImageView? {
        val imageViews = listOf(binding.iv1, binding.iv2, binding.iv3, binding.iv4, binding.iv5)
        return imageViews.find { it.id == imageViewId }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            val imageViewId = result.data?.extras?.getInt("imageId", -1)
            Log.d("Chicken", "Image Tag: $imageViewId")
            val imageView = when (imageViewId) {
                binding.iv1.id -> binding.iv1
                binding.iv2.id -> binding.iv2
                binding.iv3.id -> binding.iv3
                binding.iv4.id -> binding.iv4
                binding.iv5.id -> binding.iv5
                else -> null
            }
            myFile.let { file ->
                imageView?.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val REQUEST_CODE_IMAGE_CAPTURE = 100
        private const val TAG = "OuterWearActivity"
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}