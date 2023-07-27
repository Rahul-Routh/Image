package com.example.image

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings.Secure
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Base64
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.example.image.Base64AttachmentFile.Companion.convertAttachmentToString2
import com.example.image.databinding.ActivityDetailsClickCameraBinding
import com.github.drjacky.imagepicker.ImagePicker
import com.github.drjacky.imagepicker.constant.ImageProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import java.io.IOException
import java.security.AccessController.getContext
import java.util.Locale


class DetailsClickCameraActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailsClickCameraBinding

    var str_bitmap : String = ""

    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    var getLocation: Button? = null
    private val REQUEST_CODE = 100

    private lateinit var PERMISSIONS: Array<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details_click_camera)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        binding.button.setOnClickListener {

            PERMISSIONS = arrayOf<String>(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_PHONE_NUMBERS
            )
            if (!hasPermissions(this,PERMISSIONS.toString())) {

                ActivityCompat.requestPermissions(this,PERMISSIONS,1);

            }

        }

        binding.image.setOnClickListener {

            val builder = Dialog(it.context)
            builder.requestWindowFeature(Window.FEATURE_NO_TITLE)
            builder.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val imageView = ImageView(it.context)
            val encodeByte: ByteArray = Base64.decode(str_bitmap, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
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


    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        Toast.makeText(this, result.toString(), Toast.LENGTH_LONG).show()

        if (result.resultCode == Activity.RESULT_OK) {

            val data: Intent? = result.data

            if (data?.data != null) {
                val imageUri: Uri? = data.data

                str_bitmap = convertAttachmentToString2(this,imageUri)

                val encodeByte: ByteArray = Base64.decode(str_bitmap, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
                binding.image.setImageBitmap(bitmap)



            }

        }
    }


    private fun getLastLocation() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        fusedLocationProviderClient?.getLastLocation()
                ?.addOnSuccessListener(OnSuccessListener<Location?> { location ->
                    if (location != null) {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        var addresses: List<Address>? = null
                        try {
                            addresses =
                                geocoder.getFromLocation(location.latitude, location.longitude, 1)

                            Log.d("sdsdsds ", "sdsd " + addresses.toString())

                            binding.latitude.setText("Lagitude :" + addresses!![0].latitude)
                            binding.longitude.setText("Longitude :" + addresses!![0].longitude)
                            binding.address.setText("Address :" + addresses!![0].getAddressLine(0))
                            binding.phone.setText("Phone :" + addresses!![0].phone)
                            binding.pin.setText("Pin :" + addresses!![0].postalCode)
                            binding.city.setText("City :" + addresses!![0].locality)
                            binding.country.setText("Country :" + addresses!![0].countryName)

                            //get telephone
                            val telephone = this.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
                            binding.phone.setText("Phone :" + telephone.line1Number)
                            //val dev_id = Secure.getString(applicationContext.getContentResolver(), Secure.ANDROID_ID)
                            val dev_id = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
                            binding.imei.setText("Device Id :" +dev_id.toString())
                            binding.textDisplay.setText("Brand:" +Build.BRAND)
                            binding.textDisplay1.setText("Brand:" +Build.MODEL)


                            ImagePicker.with(this)
                                //...
                                .provider(ImageProvider.BOTH)
                                .createIntentFromDialog {
                                    launcher.launch(it)
                                }

                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                })
            return
    }


  /*  @SuppressLint("HardwareIds")
    private fun getSystemDetail(): String {
        return "Brand: ${Build.BRAND} \n" +
                "Model: ${Build.MODEL} \n"+
                "DeviceID: ${Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)} \n" +
                "ID: ${Build.ID} \n" +
                "SDK: ${Build.VERSION.SDK_INT} \n" +
                "Manufacture: ${Build.MANUFACTURER} \n" +
                "Brand: ${Build.BRAND} \n" +
                "User: ${Build.USER} \n" +
                "Type: ${Build.TYPE} \n" +
                "Base: ${Build.VERSION_CODES.BASE} \n" +
                "Incremental: ${Build.VERSION.INCREMENTAL} \n" +
                "Board: ${Build.BOARD} \n" +
                "Host: ${Build.HOST} \n" +
                "FingerPrint: ${Build.FINGERPRINT} \n" +
                "Version Code: ${Build.VERSION.RELEASE}"
    }*/

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera Permission is denied", Toast.LENGTH_SHORT).show()
            }else if (grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location Permission is denied", Toast.LENGTH_SHORT).show()
            }else if (grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "COARSE LOCATION Permission is denied", Toast.LENGTH_SHORT).show()
            }else if (grantResults[3] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "READ PHONE NUMBERS Permission is denied", Toast.LENGTH_SHORT).show()
            }else{
                getLastLocation();
                //binding.textDisplay.text = getSystemDetail()

            }
        }
    }

    private fun hasPermissions(context: Context?, vararg PERMISSIONS: String): Boolean {
        if (context != null && PERMISSIONS != null) {
            for (permission in PERMISSIONS) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }


}