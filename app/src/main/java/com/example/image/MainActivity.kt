package com.example.image

import android.R.attr.data
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.image.databinding.ActivityMainBinding
import com.github.drjacky.imagepicker.ImagePicker
import com.github.drjacky.imagepicker.constant.ImageProvider
import java.io.ByteArrayInputStream
import java.io.InputStream


class MainActivity : AppCompatActivity() {

    lateinit var uri: Uri
    var selectbutton: String = ""
    var decodedByte: String? = null
    var bitmap: Bitmap? = null
    lateinit var binding: ActivityMainBinding

    //var mArrayUri: ArrayList<Uri>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.next.setOnClickListener {
            val intent = Intent(this@MainActivity,ImageActivity::class.java)
            startActivity(intent)
        }
        binding.locationClick.setOnClickListener {
            val intent = Intent(this@MainActivity,DetailsClickCameraActivity::class.java)
            startActivity(intent)
        }



        binding.button.setOnClickListener {
            /*ImagePicker.with(this)
                //...
                .setMultipleAllowed(true)
                .provider(ImageProvider.BOTH) //Or bothCameraGallery()
                .createIntentFromDialog {
                    selectbutton = "After"
                    launcher.launch(it) }*/

            val intent = Intent(ACTION_GET_CONTENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.type = "image/*"
            launcher.launch(intent)
        }
        binding.button2.setOnClickListener {
            ImagePicker.with(this)
                //...
                .provider(ImageProvider.BOTH)
                .setMultipleAllowed(true)//Or bothCameraGallery()
                .createIntentFromDialog {
                    selectbutton = "before"
                    launcher.launch(it) }
        }
        binding.image.setOnClickListener {

            val builder = Dialog(it.context)
            builder.requestWindowFeature(Window.FEATURE_NO_TITLE)
            builder.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val imageView = ImageView(it.context)

            imageView.setImageBitmap(bitmap)
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

            imageView.setImageBitmap(bitmap)
            builder.addContentView(
                imageView, RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
            builder.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        Toast.makeText(this,result.toString(), Toast.LENGTH_LONG).show()

        if (result.resultCode == Activity.RESULT_OK) {
            //Toast.makeText(this,result.toString(), Toast.LENGTH_LONG).show()

            val data: Intent? = result.data
            //If multiple image selected
           // Toast.makeText(this,data.toString(), Toast.LENGTH_LONG).show()

            if (data?.clipData != null) {

                val count = data.clipData?.itemCount ?: 0

                for (i in 0 until count) {
                    val imageUri: Uri? = data.clipData?.getItemAt(i)?.uri
                    //val file = getImageFromUri(imageUri)
                    /*file?.let {
                        selectedPaths.add(it.absolutePath)
                    }*/
                }
                //imageAdapter.addSelectedImages(selectedPaths)
            }
            //If single image selected
            else if (data?.data != null) {
                val imageUri: Uri? = data.data
                Toast.makeText(this, "Single"+imageUri, Toast.LENGTH_LONG).show()
            }
        }

        /*if (it.resultCode == Activity.RESULT_OK) {
               // uri = it.data?.data!!
                Toast.makeText(this, uri.toString(), Toast.LENGTH_LONG).show()

                if (it.data?.getData() != null) {
                    // if single image is selected

                    var imageUri: Uri = it.data!!.data!!
                    Toast.makeText(this, "Single", Toast.LENGTH_LONG).show()

                    //   iv_image.setImageURI(imageUri) Here you can assign the picked image uri to your imageview

                } else {
                    Toast.makeText(this, "Multiple", Toast.LENGTH_LONG).show()

                }


                *//*val cout: Int = uri.getItemCount()
                for (i in 0 until cout) {
                    // adding imageuri in array
                    val imageurl: Uri = data.getClipData().getItemAt(i).getUri()
                    decodedByte = convertAttachmentToString2(this,uri)
                    mArrayUri.add(imageurl)
                }
                // setting 1st selected image into image switcher
                // setting 1st selected image into image switcher
                imageView.setImageURI(mArrayUri.get(0))
                position = 0*//*

                *//*if (selectbutton.equals("After")) {
                   // binding.image.setImageBitmap(uri)
                    val encodeByte: ByteArray = Base64.decode(decodedByte, Base64.DEFAULT)

                    val inputStream: InputStream = ByteArrayInputStream(encodeByte)
                    bitmap = BitmapFactory.decodeStream(inputStream)
                    binding.image.setImageBitmap(bitmap)
                    Log.d("dsadsad", "sadsa: "+decodedByte.toString())
                } else if (selectbutton.equals("before")) {
                    //binding.image2.setImageURI(uri)
                    val encodeByte: ByteArray = Base64.decode(decodedByte, Base64.DEFAULT)

                    val inputStream: InputStream = ByteArrayInputStream(encodeByte)
                    bitmap = BitmapFactory.decodeStream(inputStream)
                    binding.image2.setImageBitmap(bitmap)
                }*//*
            }*/
    }


}