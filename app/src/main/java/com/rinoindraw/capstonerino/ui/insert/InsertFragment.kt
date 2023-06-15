package com.rinoindraw.capstonerino.ui.insert

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.paging.ExperimentalPagingApi
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.google.android.material.snackbar.Snackbar
import com.rinoindraw.capstonerino.R
import com.rinoindraw.capstonerino.databinding.FragmentInsertBinding
import com.rinoindraw.capstonerino.ui.customview.CustomSpinnerAdapter
import com.rinoindraw.capstonerino.ui.result.ResultActivity
import com.rinoindraw.capstonerino.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

@Suppress("BlockingMethodInNonBlockingContext", "DEPRECATION")
@AndroidEntryPoint
@ExperimentalPagingApi
class InsertFragment : Fragment() {

    private lateinit var binding: FragmentInsertBinding
    private lateinit var currentPhotoPath: String

    private var getFile: File? = null
    private lateinit var pref: SessionManager
    private lateinit var token: String

    private val viewModel: InsertViewModel by viewModels()

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {

            val file = File(currentPhotoPath).also { getFile = it }
            val os: OutputStream

            val bitmap = BitmapFactory.decodeFile(getFile?.path)
            val exif = ExifInterface(currentPhotoPath)
            val orientation: Int = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )

            val rotatedBitmap: Bitmap = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> TransformationUtils.rotateImage(bitmap, 90)
                ExifInterface.ORIENTATION_ROTATE_180 -> TransformationUtils.rotateImage(bitmap, 180)
                ExifInterface.ORIENTATION_ROTATE_270 -> TransformationUtils.rotateImage(bitmap, 270)
                ExifInterface.ORIENTATION_NORMAL -> bitmap
                else -> bitmap
            }

            try {
                os = FileOutputStream(file)
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
                os.flush()
                os.close()

                getFile = file
            } catch (e: Exception) {
                e.printStackTrace()
            }

            binding.imgPreview.setImageBitmap(rotatedBitmap)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            uriToFile(selectedImg, requireContext()).also { getFile = it }

            binding.imgPreview.setImageURI(selectedImg)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInsertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = SessionManager(requireContext())
        token = pref.fetchAuthToken().toString()

        val spinner = binding.menuSpinner
        val itemList = listOf("chili", "rice", "tomato")

        val adapter = CustomSpinnerAdapter(requireContext(), itemList)
        spinner.adapter = adapter

        initAction()

    }

    private fun initAction() {

        binding.apply {

            btnCamera.setOnClickListener {
                startIntentCamera()
            }
            btnGallery.setOnClickListener {
                startIntentGallery()
            }
            btnUpload.setOnClickListener {
                uploadStory()
            }
            imgBack.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_insertFragment_to_navigation_home)
            )

        }

    }

    private fun uploadStory() {

        showLoading(true)
        var isValid = true

        if (getFile == null) {
            showSnackbar(getString(R.string.error_empty_image))
            isValid = false
        }

        if (isValid) {
            lifecycleScope.launchWhenStarted {
                launch {
                    val username = pref.getUsername().toString().toRequestBody("text/plain".toMediaTypeOrNull())
                    val plant = binding.menuSpinner.selectedItem.toString().toRequestBody("text/plain".toMediaTypeOrNull())

                    val file = MediaUtils.reduceFileImage(getFile as File)
                    val requestImageFile = file.asRequestBody("image/png".toMediaTypeOrNull())
                    val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                        "image",
                        file.name,
                        requestImageFile
                    )

                    viewModel.uploadImage(username ,plant,  imageMultipart )
                        .collect { response ->
                            response.onSuccess { resultData ->
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.message_success_upload),
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(requireContext(), ResultActivity::class.java)
                                intent.putExtra(ResultActivity.EXTRA_RESULT_DATA, resultData)
                                startActivity(intent)
                                requireActivity().finish()
                            }
                            response.onFailure {
                                showLoading(false)
                                showSnackbar(getString(R.string.message_failed_upload))
                            }
                        }
                }
            }

        } else showLoading(false)
    }

    private fun startIntentGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun startIntentCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(requireActivity().packageManager)

        MediaUtils.createTempFile(requireActivity().application).also {
            val photoUri = FileProvider.getUriForFile(
                requireContext(),
                "com.rinoindraw.capstonerino",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            launcherIntentCamera.launch(intent)
        }
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.show() else binding.progressBar.gone()
        if (isLoading) binding.bgDim.show() else binding.bgDim.gone()
        binding.apply {
            btnUpload.isClickable = !isLoading
            btnUpload.isEnabled = !isLoading
            btnGallery.isClickable = !isLoading
            btnGallery.isEnabled = !isLoading
            btnCamera.isClickable = !isLoading
            btnCamera.isEnabled = !isLoading
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }

}