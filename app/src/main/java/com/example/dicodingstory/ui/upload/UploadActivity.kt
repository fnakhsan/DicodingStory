package com.example.dicodingstory.ui.upload

import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.example.dicodingstory.R
import com.example.dicodingstory.data.Result
import com.example.dicodingstory.databinding.ActivityUploadBinding
import com.example.dicodingstory.utils.Utils.createTempFile
import com.example.dicodingstory.utils.Utils.reduceFileImage
import com.example.dicodingstory.utils.Utils.uriToFile
import com.example.dicodingstory.utils.ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var currentPhotoPath: String
    private var getFile: File? = null
    private var location: Location? = null

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

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                permissions[permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                else -> {
                    binding.switchLocation.isChecked = false
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory = ViewModelFactory.getInstance(this)
        val uploadViewModel: UploadViewModel by viewModels {
            factory
        }
        binding.apply {
            btnCamera.setOnClickListener { startTakePhoto() }
            btnGallery.setOnClickListener { startGallery() }
            switchLocation.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                if (isChecked) {
                    lifecycleScope.launch {
                        getMyLastLocation()
                    }
                } else {
                    location = null
                }
            }
            uploadViewModel.getToken().observe(this@UploadActivity) { token ->
                buttonAdd.setOnClickListener { uploadImage(token, uploadViewModel, location) }
            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@UploadActivity)
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun getMyLastLocation() {
        if (checkPermission(permission.ACCESS_FINE_LOCATION) &&
            checkPermission(permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    this.location = location
                } else {
                    Toast.makeText(
                        this@UploadActivity,
                        R.string.error_no_location,
                        Toast.LENGTH_SHORT
                    ).show()

                    binding.switchLocation.isChecked = false
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    permission.ACCESS_FINE_LOCATION,
                    permission.ACCESS_COARSE_LOCATION
                )
            )
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

    private fun uploadImage(token: String?, uploadViewModel: UploadViewModel, location: Location?) {
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
            uploadViewModel.uploadStory(token, description, imageMultipart, location)
                .observe(this) {
                    when (it) {
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