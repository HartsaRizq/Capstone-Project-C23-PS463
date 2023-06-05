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
    private var getFile: File? = null
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
                binding.ivOut1.id -> binding.ivOut1
                binding.ivOut2.id -> binding.ivOut2
                binding.ivOut3.id -> binding.ivOut3
                binding.ivOut4.id -> binding.ivOut4
                binding.ivOut5.id -> binding.ivOut5
                binding.ivIn1.id -> binding.ivIn1
                binding.ivIn2.id -> binding.ivIn2
                binding.ivIn3.id -> binding.ivIn3
                binding.ivIn4.id -> binding.ivIn4
                binding.ivIn5.id -> binding.ivIn5
                binding.ivPants1.id -> binding.ivPants1
                binding.ivPants2.id -> binding.ivPants2
                binding.ivPants3.id -> binding.ivPants3
                binding.ivPants4.id -> binding.ivPants4
                binding.ivPants5.id -> binding.ivPants5
                else -> null
            }
            myFile.let { file ->
                getFile = file
                imageView?.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
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
                binding.ivOut1.id -> binding.ivOut1
                binding.ivOut2.id -> binding.ivOut2
                binding.ivOut3.id -> binding.ivOut3
                binding.ivOut4.id -> binding.ivOut4
                binding.ivOut5.id -> binding.ivOut5
                binding.ivIn1.id -> binding.ivIn1
                binding.ivIn2.id -> binding.ivIn2
                binding.ivIn3.id -> binding.ivIn3
                binding.ivIn4.id -> binding.ivIn4
                binding.ivIn5.id -> binding.ivIn5
                binding.ivPants1.id -> binding.ivPants1
                binding.ivPants2.id -> binding.ivPants2
                binding.ivPants3.id -> binding.ivPants3
                binding.ivPants4.id -> binding.ivPants4
                binding.ivPants5.id -> binding.ivPants5
                else -> null
            }
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@UploadActivity)
                getFile = myFile
                imageView?.setImageURI(uri)
            }
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