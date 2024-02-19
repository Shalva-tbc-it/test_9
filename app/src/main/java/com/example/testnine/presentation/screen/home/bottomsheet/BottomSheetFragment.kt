package com.example.testnine.presentation.screen.home.bottomsheet

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.testnine.databinding.FragmentBottomsheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class BottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBottomsheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomsheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCamera.setOnClickListener {
            takePicture()
        }
        binding.btnGalleria.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            d("PhotoPicker", "Selected URI: $uri")
            getFromGalleriaImage(uri)
            binding.image.setImageBitmap(getFromGalleriaImage(uri))


        } else {
            d("PhotoPicker", "No media selected")
        }
    }

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                d("PhotoPickerCamera", "$data")

                val imageBitmap = data?.extras?.get("data") as Bitmap
                getFromCameraBitmap(imageBitmap)
                binding.image.setImageBitmap(getFromCameraBitmap(imageBitmap))
            }
        }

    private fun takePicture() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureLauncher.launch(takePictureIntent)
    }

    private fun getFromCameraBitmap(bitmap: Bitmap): Bitmap {
        val getIntent = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, getIntent)
        val stream = getIntent.toByteArray()
        return BitmapFactory.decodeByteArray(stream, 0, stream.size)
    }

    private fun getFromGalleriaImage(uri: Uri): Bitmap? {
        val getUri = requireContext().contentResolver.openInputStream(uri)
        getUri?.use { input ->
            val bitmap = BitmapFactory.decodeStream(input)
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
            return BitmapFactory.decodeStream(stream.toByteArray().inputStream())
        }
        return null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}