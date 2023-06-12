package com.dicoding.wearit.ui

//import com.dicoding.wearit.ApiConfig
//import com.dicoding.wearit.FileUploadResponse
import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.dicoding.wearit.MainActivity
import com.dicoding.wearit.databinding.ActivityUploadBinding
import com.dicoding.wearit.databinding.DialogLoadingBinding
import com.dicoding.wearit.uriToBitmap
import java.io.File

class UploadActivity : AppCompatActivity() {
    private var outwear1: Bitmap? = null
    private var outwear2: Bitmap? = null
    private var outwear3: Bitmap? = null
    private var outwear4: Bitmap? = null
    private var outwear5: Bitmap? = null
    private var innerwear1: Bitmap? = null
    private var innerwear2: Bitmap? = null
    private var innerwear3: Bitmap? = null
    private var innerwear4: Bitmap? = null
    private var innerwear5: Bitmap? = null
    private var bottomwear1: Bitmap? = null
    private var bottomwear2: Bitmap? = null
    private var bottomwear3: Bitmap? = null
    private var bottomwear4: Bitmap? = null
    private var bottomwear5: Bitmap? = null
    private lateinit var binding: ActivityUploadBinding
    private lateinit var currentPhotoPath: String
    private var currentPhotoId: Int = 0
    private lateinit var loadingDialog: AlertDialog
    private lateinit var dialogLoadingBinding: DialogLoadingBinding
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
        binding.btnUpload.setOnClickListener { uploadImage() }
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
                    outwear1 = BitmapFactory.decodeFile(myFile.path)
                    binding.ivOut1
                }
                binding.ivOut2.id -> {
                    outwear2 = BitmapFactory.decodeFile(myFile.path)
                    binding.ivOut2
                }
                binding.ivOut3.id -> {
                    outwear3 = BitmapFactory.decodeFile(myFile.path)
                    binding.ivOut3
                }
                binding.ivOut4.id -> {
                    outwear4 = BitmapFactory.decodeFile(myFile.path)
                    binding.ivOut4
                }
                binding.ivOut5.id -> {
                    outwear5 = BitmapFactory.decodeFile(myFile.path)
                    binding.ivOut5
                }
                binding.ivIn1.id -> {
                    innerwear1 = BitmapFactory.decodeFile(myFile.path)
                    binding.ivIn1
                }
                binding.ivIn2.id -> {
                    innerwear2 = BitmapFactory.decodeFile(myFile.path)
                    binding.ivIn2
                }
                binding.ivIn3.id -> {
                    innerwear3 = BitmapFactory.decodeFile(myFile.path)
                    binding.ivIn3
                }
                binding.ivIn4.id -> {
                    innerwear4 = BitmapFactory.decodeFile(myFile.path)
                    binding.ivIn4
                }
                binding.ivIn5.id -> {
                    innerwear5 = BitmapFactory.decodeFile(myFile.path)
                    binding.ivIn5
                }
                binding.ivPants1.id -> {
                    bottomwear1 = BitmapFactory.decodeFile(myFile.path)
                    binding.ivPants1
                }
                binding.ivPants2.id -> {
                    bottomwear2 = BitmapFactory.decodeFile(myFile.path)
                    binding.ivPants2
                }
                binding.ivPants3.id -> {
                    bottomwear3 = BitmapFactory.decodeFile(myFile.path)
                    binding.ivPants3
                }
                binding.ivPants4.id -> {
                    bottomwear4 = BitmapFactory.decodeFile(myFile.path)
                    binding.ivPants4
                }
                binding.ivPants5.id -> {
                    bottomwear5 = BitmapFactory.decodeFile(myFile.path)
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
                    outwear1 = uriToBitmap(selectedImg, this@UploadActivity)
                    binding.ivOut1
                }
                binding.ivOut2.id -> {
                    outwear2 = uriToBitmap(selectedImg, this@UploadActivity)
                    binding.ivOut2
                }
                binding.ivOut3.id -> {
                    outwear3 = uriToBitmap(selectedImg, this@UploadActivity)
                    binding.ivOut3
                }
                binding.ivOut4.id -> {
                    outwear4 = uriToBitmap(selectedImg, this@UploadActivity)
                    binding.ivOut4
                }
                binding.ivOut5.id -> {
                    outwear5 = uriToBitmap(selectedImg, this@UploadActivity)
                    binding.ivOut5
                }
                binding.ivIn1.id -> {
                    innerwear1 = uriToBitmap(selectedImg, this@UploadActivity)
                    binding.ivIn1
                }
                binding.ivIn2.id -> {
                    innerwear2 = uriToBitmap(selectedImg, this@UploadActivity)
                    binding.ivIn2
                }
                binding.ivIn3.id -> {
                    innerwear3 = uriToBitmap(selectedImg, this@UploadActivity)
                    binding.ivIn3
                }
                binding.ivIn4.id -> {
                    innerwear4 = uriToBitmap(selectedImg, this@UploadActivity)
                    binding.ivIn4
                }
                binding.ivIn5.id -> {
                    innerwear5 = uriToBitmap(selectedImg, this@UploadActivity)
                    binding.ivIn5
                }
                binding.ivPants1.id -> {
                    bottomwear1 = uriToBitmap(selectedImg, this@UploadActivity)
                    binding.ivPants1
                }
                binding.ivPants2.id -> {
                    bottomwear2 = uriToBitmap(selectedImg, this@UploadActivity)
                    binding.ivPants2
                }
                binding.ivPants3.id -> {
                    bottomwear3 = uriToBitmap(selectedImg, this@UploadActivity)
                    binding.ivPants3
                }
                binding.ivPants4.id -> {
                    bottomwear4 = uriToBitmap(selectedImg, this@UploadActivity)
                    binding.ivPants4
                }
                binding.ivPants5.id -> {
                    bottomwear5 = uriToBitmap(selectedImg, this@UploadActivity)
                    binding.ivPants5
                }
                else -> null
            }
            imageView?.setImageURI(selectedImg)
        }
    }

    private fun showLoadingDialog() {
        val builder = AlertDialog.Builder(this)
        dialogLoadingBinding = DialogLoadingBinding.inflate(layoutInflater)
        builder.setView(dialogLoadingBinding.root)
        builder.setCancelable(false)
        loadingDialog = builder.create()
        loadingDialog.show()
    }

    private fun uploadImage() {
        val outwearImages = arrayOf(outwear1, outwear2, outwear3, outwear4, outwear5)
        val innerwearImages = arrayOf(innerwear1, innerwear2, innerwear3, innerwear4, innerwear5)
        val bottomwearImages = arrayOf(bottomwear1, bottomwear2, bottomwear3, bottomwear4, bottomwear5)
        val allImagesNotNull = outwearImages.all { it != null } &&
                innerwearImages.all { it != null } &&
                bottomwearImages.all { it != null }

        if (allImagesNotNull) {
            showLoadingDialog()

            Handler(Looper.getMainLooper()).postDelayed({
                if (::loadingDialog.isInitialized && loadingDialog.isShowing) {
                    loadingDialog.dismiss()
                }

                // Go to MainActivity
                val intent = Intent(this@UploadActivity, MainActivity::class.java)
                intent.putExtra("uploaded", true)
                startActivity(intent)
                finish()
            }, 10000) // 10 seconds delay
        } else {
            Toast.makeText(this, "Need all images to be inputted", Toast.LENGTH_SHORT).show()
        }
    }

    /* This function is used for TfLite but I failed to finished it by the time we have left
    private fun uploadImage() {
        val model = ConvertedModel.newInstance(this)
        lateinit var reducedBitmap: Bitmap

        if (outwear1 != null) {
            reducedBitmap = reduceBitmapImage(outwear1!!)
            binding.ivOut2.setImageBitmap(reducedBitmap)
            val resized: Bitmap = Bitmap.createScaledBitmap(reducedBitmap, 224, 224, false)
            Log.d("Resized", "Width: ${resized.width}, Height: ${resized.height}")
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3)
            , DataType.FLOAT32)
            Log.d("TensorBuffer", "Shape: ${inputFeature0.shape.contentToString()}
            , Size: ${inputFeature0.flatSize}")
            var tbuffer = TensorImage.fromBitmap(resized)
            var byteBuffer = tbuffer.buffer

            inputFeature0.loadBuffer(byteBuffer)

            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer

            Log.d("OutputTF", outputFeature0.toString())
            model.close()
        }
    }
    */

    /* This function was intentionally to be used if CC finished the task
    private fun uploadImage() {
        val apiService = ApiConfig().getApiService()
        val uploadImageRequests = mutableListOf<Call<FileUploadResponse>>()

        val files = listOf(
            outwear1, outwear2, outwear3, outwear4, outwear5,
            innerwear1, innerwear2, innerwear3, innerwear4, innerwear5,
            bottomwear1, bottomwear2, bottomwear3, bottomwear4, bottomwear5
        )

        for (file in files) {
            if (file != null) {
                val reducedFile = reduceFileImage(file)
                val requestImageFile = reducedFile.asRequestBody("image/jpeg".toMediaType())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    reducedFile.name,
                    requestImageFile
                )

                val uploadImageRequest = apiService.uploadImage(imageMultipart)
                uploadImageRequests.add(uploadImageRequest)
            }
        }

        if (uploadImageRequests.isNotEmpty()) {
            for (uploadImageRequest in uploadImageRequests) {
                uploadImageRequest.enqueue(object : Callback<FileUploadResponse> {
                    override fun onResponse(
                        call: Call<FileUploadResponse>,
                        response: Response<FileUploadResponse>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            if (responseBody != null && !responseBody.error) {
                                Toast.makeText(
                                    this@UploadActivity,
                                    responseBody.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this@UploadActivity,
                                response.message(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                        Toast.makeText(this@UploadActivity, t.message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
        } else {
            Toast.makeText(
                this@UploadActivity,
                "Silakan masukkan berkas gambar terlebih dahulu.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
     */



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
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}