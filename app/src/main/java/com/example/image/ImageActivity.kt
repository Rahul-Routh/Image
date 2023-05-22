package com.example.image

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.image.databinding.ActivityImageBinding
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File


class ImageActivity : AppCompatActivity() {

    var activityResultLauncher: ActivityResultLauncher<Intent>? = null
    var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>? = null

    var selectedDocument: String = ""
    var selectbutton: String = ""

    var havw_BeforeWorkPhoto: String = ""
    var havw_AfterWorkPhoto: String = ""

    var Url_BeforeWorkPhoto: String = ""
    var Url_AfterWorkPhoto: String = ""

    var havw_BeforeWorkPhotoName: String = ""
    var havw_AfterWorkPhotoName: String = ""

    private val REQUEST_GALLERY_CADE = 100
    private val CAMERA_PERMISSION_CODE = 101
    lateinit var binding: ActivityImageBinding
    lateinit var decodedByte:Bitmap
    lateinit var filepath:Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_image)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image)

        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_GALLERY_CADE)

        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (selectedDocument == "selectImage") {
                if (selectbutton == "ChooseBefore") {
                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: $uri")
                        val file = File(uri.toString())

                        havw_BeforeWorkPhoto = Base64AttachmentFile.convertAttachmentToString(
                            this, uri
                        ).toString()

                        Log.d("imagedatDetails", havw_BeforeWorkPhoto)

                        //binding1.textChooseBefore.text = file.name
                        //binding1.txtImageNameBefore.text = file.name
                        havw_BeforeWorkPhotoName = file.name

                        //binding1.imageView.setImageURI(uri)
                        Url_BeforeWorkPhoto = uri.toString()

                        val base64String =
                            "data:image/png;base64,"+havw_BeforeWorkPhoto
                        val base64Image = base64String.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray()[1]
                        val decodedString = Base64.decode(base64Image, Base64.DEFAULT)
                        decodedByte =
                            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

                        binding.image.setImageBitmap(decodedByte)

                    } else {
                        Log.d("PhotoPicker", "No media selected")
                    }
                } else if (selectbutton == "ChooseAfter") {
                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: $uri")
                        val file = File(uri.toString())

                        havw_AfterWorkPhoto = Base64AttachmentFile.convertAttachmentToString(
                            this, uri
                        ).toString()

                        Log.d("imagedatDetails", havw_AfterWorkPhoto)

                        //binding1.textChooseAfter.text = file.name
                        //binding1.txtImageNameAfter.text = file.name
                        havw_AfterWorkPhotoName = file.name

                        Url_AfterWorkPhoto = uri.toString()
                        //binding1.imageView.setImageURI(uri)
                    } else {
                        Log.d("PhotoPicker", "No media selected")
                    }
                    val base64String =
                        "data:image/png;base64,"+havw_AfterWorkPhoto
                    val base64Image = base64String.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()[1]
                    val decodedString = Base64.decode(base64Image, Base64.DEFAULT)
                    decodedByte =
                        BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

                    binding.image.setImageBitmap(decodedByte)
                }

            }
        }

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->

            if (selectedDocument == "selectCamera") {
                if (selectbutton == "ChooseBefore") {
                    val bundle = result.data!!.extras
                    val bitmap = bundle!!["data"] as Bitmap?
                    val baos = ByteArrayOutputStream()

                    bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    val decoded =
                        BitmapFactory.decodeStream(ByteArrayInputStream(baos.toByteArray()))

                    Log.e("Compressed dimensions", decoded.width.toString() + " " + decoded.height)

                    val path: String = MediaStore.Images.Media.insertImage(
                        applicationContext.getContentResolver(), bitmap, "Title", null)

                    filepath = Uri.parse(path)

                    Log.d("sdsad",""+filepath)

                    binding.image.setImageURI(filepath)
                }
                if (selectbutton == "ChooseAfter") {

                }
            }
        }


        binding.button.setOnClickListener {
            selectbutton = "ChooseBefore"
            startDialog()
        }
        binding.button2.setOnClickListener {
            selectbutton = "ChooseAfter"
            startDialog()
        }

        binding.image.setOnClickListener {

            val builder = Dialog(it.context)
            builder.requestWindowFeature(Window.FEATURE_NO_TITLE)
            builder.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val imageView = ImageView(it.context)

            imageView.setImageURI(filepath)
            builder.addContentView(
                imageView, RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
            builder.show()
        }
        binding.image2.setOnClickListener {

            val builder = Dialog(it.context)
            builder.requestWindowFeature(Window.FEATURE_NO_TITLE)
            builder.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val imageView = ImageView(it.context)

            imageView.setImageBitmap(decodedByte)
            builder.addContentView(
                imageView, RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
            builder.show()
        }

    }

    private fun startDialog() {

        val items = arrayOf<CharSequence>("Choose Images", "Select Camera", "Cancel")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add Photo")
        builder.setItems(items) { dialog, item ->

            if (items[item] == "Choose Images") {
                checkPermission(Manifest.permission.READ_MEDIA_IMAGES, REQUEST_GALLERY_CADE)
                selectedDocument = "selectImage"

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2) {

                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_MEDIA_IMAGES
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {

                        pickMedia?.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

                    } else {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ), REQUEST_GALLERY_CADE
                        )
                    }
                } else {

                    pickMedia?.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

//                    val gallery_intent = Intent(MediaStore.ACTION_PICK_IMAGES)
//                    //intent.addCategory(Intent.CATEGORY_OPENABLE)
//                    gallery_intent.type = "image/*"
//                    gallery_intent.setAction(Intent.ACTION_GET_CONTENT);
//                    activityResultLauncher?.launch(gallery_intent)

                }
            } else if (items[item] == "Select Camera") {

                checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE)

                selectedDocument = "selectCamera"
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                activityResultLauncher?.launch(cameraIntent)

                //    openCameraIntent();
            } else if (items[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    // Function to check and request permission.
    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_DENIED
        ) {
            // Requesting the permission
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        } else {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show()
        }
    }

    // This function is called when the user accepts or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when the user is prompt for permission.

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera Permission Granted", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT)
                    .show()
            }
        } else if (requestCode == REQUEST_GALLERY_CADE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}