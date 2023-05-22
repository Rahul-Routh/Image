package com.example.image

import android.app.Activity
import android.app.Dialog
import android.content.Intent
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.image.Base64AttachmentFile.Companion.convertAttachmentToString2
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.next.setOnClickListener {
            val intent = Intent(this@MainActivity,ImageActivity::class.java)
            startActivity(intent)
        }


        binding.button.setOnClickListener {
            ImagePicker.with(this)
                //...
                .provider(ImageProvider.BOTH) //Or bothCameraGallery()
                .createIntentFromDialog {
                    selectbutton = "After"
                    launcher.launch(it) }
        }
        binding.button2.setOnClickListener {
            ImagePicker.with(this)
                //...
                .provider(ImageProvider.BOTH) //Or bothCameraGallery()
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

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                uri = it.data?.data!!

                decodedByte = convertAttachmentToString2(this,uri)

                if (selectbutton.equals("After")) {
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
                }
            }
        }

}