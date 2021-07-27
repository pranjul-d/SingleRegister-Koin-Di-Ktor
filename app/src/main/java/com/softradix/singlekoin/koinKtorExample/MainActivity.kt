package com.softradix.singlekoin.koinKtorExample

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.softradix.singlekoin.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var requestType: String = "requestType"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            requestType = "camera"
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            commonResultActivity.launch(intent)
        }
        button2.setOnClickListener {
            requestType = "file"
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
            }

            commonResultActivity.launch(Intent.createChooser(intent, "Choose a file"))
        }
    }

    val commonResultActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent = result.data!!
            if (requestType == "camera") {
                val imageBitmap = data.extras?.get("data") as Bitmap
                imageView.setImageBitmap(imageBitmap)
                UtilityExtensions.saveImageToStorage(this, bitmap = imageBitmap,)
            }
            if (requestType == "file") {
                val video: Uri = data.data!!
                Log.e("PATH-->", UtilityExtensions.getPathFromURI(this, video) as String)

                /*  val projection = arrayOf(MediaStore.Files.FileColumns.DATA)
                  val cursor: Cursor? = contentResolver.query(video, projection,
                      null, null, null)

                  if (cursor != null) {
                  val columnIndex: Int = cursor.getColumnIndexOrThrow(projection[0])
                      cursor.moveToFirst()
                      cursor.close()
                      Log.d("PATH-->", cursor.getString(columnIndex))
                  }*/
            }
        }

    }

}