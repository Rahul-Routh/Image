package com.example.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.widget.ImageView
import java.io.ByteArrayOutputStream
import java.io.IOException

class Base64AttachmentFile {

    companion object{

        fun convertImageToString(capturedImage: ImageView): String? {

            capturedImage.buildDrawingCache()
            val bitmap = capturedImage.drawingCache
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream)
            val b = byteArrayOutputStream.toByteArray()
            return Base64.encodeToString(b, Base64.DEFAULT)

        }

        fun convertStringTOBitmap(stringBase64: String?): Bitmap? {

            val decodedString = Base64.decode(stringBase64, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

        }

        fun convertAttachmentToString(context: Context, attachmentURI: Uri?): String? {

            var imgByte = ByteArray(0)
            try {
                var bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, attachmentURI)
                val baos = ByteArrayOutputStream()
                //bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos) //bm is the bitmap object
                imgByte = baos.toByteArray()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return Base64.encodeToString(imgByte, Base64.DEFAULT)

        }

        fun convertAttachmentToString2(context: Context, attachmentURI: Uri?): String? {

            var imgByte = ByteArray(0)
            try {
                var bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, attachmentURI)
                val baos = ByteArrayOutputStream()
                bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    100,
                    baos
                ) //bm is the bitmap object
                imgByte = baos.toByteArray()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            var sad = Base64.encodeToString(imgByte, Base64.DEFAULT)

            val base64String = "data:image/png;base64,"+sad
            val base64Image =
                base64String.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()[1]
            val decodedString = Base64.decode(base64Image, Base64.DEFAULT)
            val decodedByte =
                BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

            return Base64.encodeToString(decodedString, Base64.DEFAULT)

        }
    }


}