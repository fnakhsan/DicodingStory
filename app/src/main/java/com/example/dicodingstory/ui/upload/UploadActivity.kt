package com.example.dicodingstory.ui.upload

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.dicodingstory.R
import com.example.dicodingstory.data.Result
import com.example.dicodingstory.databinding.ActivityUploadBinding
import com.example.dicodingstory.utils.Utils.createTempFile
import com.example.dicodingstory.utils.Utils.reduceFileImage
import com.example.dicodingstory.utils.Utils.uriToFile
import com.example.dicodingstory.utils.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private val factory = ViewModelFactory.getInstance(this)
    private val uploadViewModel: UploadViewModel by viewModels {
        factory
    }

    private lateinit var currentPhotoPath: String
    private var getFile: File? = null

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            val result = BitmapFactory.decodeFile(myFile.path)
            getFile = myFile
            binding.ivThumbnail.setImageBitmap(result)
        }
    }
    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@UploadActivity)
            getFile = myFile
            binding.ivThumbnail.setImageURI(selectedImg)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnCamera.setOnClickListener { startTakePhoto() }
        binding.btnGallery.setOnClickListener { startGallery() }
        uploadViewModel.getToken().observe(this) { token ->
            binding.buttonAdd.setOnClickListener { uploadImage(token) }
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@UploadActivity,
                "com.example.dicodingstory",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun uploadImage(token: String?) {
        showLoading(true)
        val description = binding.edAddDescription.text.toString()

        if (getFile == null) {
            Toast.makeText(
                this@UploadActivity,
                R.string.error_no_image,
                Toast.LENGTH_SHORT
            ).show()
            showLoading(false)
        } else if (description.isBlank()) {
            Toast.makeText(
                this@UploadActivity,
                R.string.error_no_description,
                Toast.LENGTH_SHORT
            ).show()
            showLoading(false)
        } else if (token != null) {
            val file = reduceFileImage(getFile as File)
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            uploadViewModel.uploadStory(token, description, imageMultipart).observe(this) {
                when(it) {
                    is Result.Loading -> {
                        Toast.makeText(
                            this@UploadActivity,
                            R.string.loading,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is Result.Success -> {
                        showLoading(false)
                        Toast.makeText(
                            this@UploadActivity,
                            R.string.success_upload,
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(
                            this@UploadActivity,
                            R.string.error_upload,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}