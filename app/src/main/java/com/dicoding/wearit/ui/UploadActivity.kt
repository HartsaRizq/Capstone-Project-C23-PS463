package com.dicoding.wearit.ui

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
//import com.dicoding.wearit.ApiConfig
import com.dicoding.wearit.Clothes
//import com.dicoding.wearit.FileUploadResponse
import com.dicoding.wearit.R
import com.dicoding.wearit.databinding.ActivityUploadBinding
import com.dicoding.wearit.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UploadActivity : AppCompatActivity() {
    private var outwear1: File? = null
    private var outwear2: File? = null
    private var outwear3: File? = null
    private var outwear4: File? = null
    private var outwear5: File? = null
    private var innerwear1: File? = null
    private var innerwear2: File? = null
    private var innerwear3: File? = null
    private var innerwear4: File? = null
    private var innerwear5: File? = null
    private var bottomwear1: File? = null
    private var bottomwear2: File? = null
    private var bottomwear3: File? = null
    private var bottomwear4: File? = null
    private var bottomwear5: File? = null
    private lateinit var binding: ActivityUploadBinding
    private lateinit var currentPhotoPath: String
    private var currentPhotoId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        setupImageViewClickListeners()
//        binding.btnUpload.setOnClickListener { uploadImage() }
    }

    private fun setupImageViewClickListeners() {
        val imageViews = listOf(binding.ivOut1, binding.ivOut2, binding.ivOut3, binding.ivOut4,
            binding.ivOut5, binding.ivIn1, binding.ivIn2, binding.ivIn3, binding.ivIn4,
            binding.ivIn5, binding.ivPants1, binding.ivPants2, binding.ivPants3, binding.ivPants4,
            binding.ivPants5)

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
                options[item] == "Choose from Gallery" -> startGallery(imageView)
                options[item] == "Cancel" -> dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun startTakePhoto(imageView: ImageView) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        val imageId = imageView.id
        Log.d("Ayam", "Image Tag: $imageId")
        com.dicoding.wearit.createTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@UploadActivity,
                "com.dicoding.wearit",
                it
            )
            currentPhotoPath = it.absolutePath
            currentPhotoId = imageId
            intent.putExtra("imageId", currentPhotoId)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery(imageView: ImageView) {
        val intent = Intent()
        val imageId = imageView.id
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        currentPhotoId = imageId
        intent.putExtra("imageId", currentPhotoId)
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            val imageViewId = result.data?.getIntExtra("imageId", currentPhotoId)
            Log.d("Chicken", "Image Tag: $imageViewId")
            val imageView = when (imageViewId) {
                binding.ivOut1.id -> {
                    outwear1 = myFile
                    binding.ivOut1
                }
                binding.ivOut2.id -> {
                    outwear2 = myFile
                    binding.ivOut2
                }
                binding.ivOut3.id -> {
                    outwear3 = myFile
                    binding.ivOut3
                }
                binding.ivOut4.id -> {
                    outwear4 = myFile
                    binding.ivOut4
                }
                binding.ivOut5.id -> {
                    outwear5 = myFile
                    binding.ivOut5
                }
                binding.ivIn1.id -> {
                    innerwear1 = myFile
                    binding.ivIn1
                }
                binding.ivIn2.id -> {
                    innerwear2 = myFile
                    binding.ivIn2
                }
                binding.ivIn3.id -> {
                    innerwear3 = myFile
                    binding.ivIn3
                }
                binding.ivIn4.id -> {
                    innerwear4 = myFile
                    binding.ivIn4
                }
                binding.ivIn5.id -> {
                    innerwear5 = myFile
                    binding.ivIn5
                }
                binding.ivPants1.id -> {
                    bottomwear1 = myFile
                    binding.ivPants1
                }
                binding.ivPants2.id -> {
                    bottomwear2 = myFile
                    binding.ivPants2
                }
                binding.ivPants3.id -> {
                    bottomwear3 = myFile
                    binding.ivPants3
                }
                binding.ivPants4.id -> {
                    bottomwear4 = myFile
                    binding.ivPants4
                }
                binding.ivPants5.id -> {
                    bottomwear5 = myFile
                    binding.ivPants5
                }
                else -> null
            }
            imageView?.setImageBitmap(BitmapFactory.decodeFile(myFile.path))
        }
    }


    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            val imageViewId = result.data?.getIntExtra("imageId", currentPhotoId)
            Log.d("Chicken", "Image Tag: $imageViewId")
            val imageView = when (imageViewId) {
                binding.ivOut1.id -> {
                    outwear1 = uriToFile(selectedImg, this@UploadActivity)
                    binding.ivOut1
                }
                binding.ivOut2.id -> {
                    outwear2 = uriToFile(selectedImg, this@UploadActivity)
                    binding.ivOut2
                }
                binding.ivOut3.id -> {
                    outwear3 = uriToFile(selectedImg, this@UploadActivity)
                    binding.ivOut3
                }
                binding.ivOut4.id -> {
                    outwear4 = uriToFile(selectedImg, this@UploadActivity)
                    binding.ivOut4
                }
                binding.ivOut5.id -> {
                    outwear5 = uriToFile(selectedImg, this@UploadActivity)
                    binding.ivOut5
                }
                binding.ivIn1.id -> {
                    innerwear1 = uriToFile(selectedImg, this@UploadActivity)
                    binding.ivIn1
                }
                binding.ivIn2.id -> {
                    innerwear2 = uriToFile(selectedImg, this@UploadActivity)
                    binding.ivIn2
                }
                binding.ivIn3.id -> {
                    innerwear3 = uriToFile(selectedImg, this@UploadActivity)
                    binding.ivIn3
                }
                binding.ivIn4.id -> {
                    innerwear4 = uriToFile(selectedImg, this@UploadActivity)
                    binding.ivIn4
                }
                binding.ivIn5.id -> {
                    innerwear5 = uriToFile(selectedImg, this@UploadActivity)
                    binding.ivIn5
                }
                binding.ivPants1.id -> {
                    bottomwear1 = uriToFile(selectedImg, this@UploadActivity)
                    binding.ivPants1
                }
                binding.ivPants2.id -> {
                    bottomwear2 = uriToFile(selectedImg, this@UploadActivity)
                    binding.ivPants2
                }
                binding.ivPants3.id -> {
                    bottomwear3 = uriToFile(selectedImg, this@UploadActivity)
                    binding.ivPants3
                }
                binding.ivPants4.id -> {
                    bottomwear4 = uriToFile(selectedImg, this@UploadActivity)
                    binding.ivPants4
                }
                binding.ivPants5.id -> {
                    bottomwear5 = uriToFile(selectedImg, this@UploadActivity)
                    binding.ivPants5
                }
                else -> null
            }
            imageView?.setImageURI(selectedImg)
        }
    }


//    private fun uploadImage() {
//        if (getFile != null) {
//            val file = reduceFileImage(getFile as File)
//
//            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
//            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
//                "photo",
//                file.name,
//                requestImageFile
//            )
//            val apiService = ApiConfig().getApiService()
//            val uploadImageRequest = apiService.uploadImage(imageMultipart)
//            uploadImageRequest.enqueue(object : Callback<FileUploadResponse> {
//                override fun onResponse(
//                    call: Call<FileUploadResponse>,
//                    response: Response<FileUploadResponse>
//                ) {
//                    if (response.isSuccessful) {
//                        val responseBody = response.body()
//                        if (responseBody != null && !responseBody.error) {
//                            Toast.makeText(this@UploadActivity, responseBody.message, Toast.LENGTH_SHORT).show()
//                        }
//                    } else {
//                        Toast.makeText(this@UploadActivity, response.message(), Toast.LENGTH_SHORT).show()
//                    }
//                }
//                override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
//                    Toast.makeText(this@UploadActivity, t.message, Toast.LENGTH_SHORT).show()
//                }
//            })
//        } else {
//            Toast.makeText(this@UploadActivity, "Silakan masukkan berkas gambar terlebih dahulu.", Toast.LENGTH_SHORT).show()
//        }
//    }

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

    private fun reduceFileImage(file: File): File {
        return file
    }

    companion object {
        private const val REQUEST_CODE_IMAGE_CAPTURE = 100
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}