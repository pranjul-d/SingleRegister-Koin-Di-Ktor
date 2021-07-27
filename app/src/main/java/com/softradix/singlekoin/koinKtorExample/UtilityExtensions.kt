package com.softradix.singlekoin.koinKtorExample

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class UtilityExtensions {
    companion object {
        fun getPathFromURI(context: Context, uri: Uri): Any {
            var realPath = String()
            uri.path?.let { path ->
                Log.e("TAG", "getPathFromURI: $path" )
                val databaseUri: Uri
                val selection: String?
                val selectionArgs: Array<String>?
                if (path.contains("/document/image:")) { // files selected from "Documents"
                    databaseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    selection = "_id=?"
                    selectionArgs = arrayOf(DocumentsContract.getDocumentId(uri).split(":")[1])
                } else {
                    // files selected from all other sources, especially on Samsung devices
                    databaseUri = uri
                    selection = null
                    selectionArgs = null
                }

                try {
                    val column = "_data"
                    val projection = arrayOf(column)
                    val cursor = context.contentResolver.query(
                        databaseUri,
                        projection,
                        selection,
                        selectionArgs,
                        null
                    )
                    cursor?.let {
                        if (it.moveToFirst()) {
                            val columnIndex = cursor.getColumnIndexOrThrow(column)
                            realPath = cursor.getString(columnIndex)
                        }
                        cursor.close()
                    }
                } catch (e: Exception) {
                    println(e)
                }
            }
            return realPath
        }

        fun saveImageToStorage(
            context: Context,
            bitmap: Bitmap,
            filename: String = "screenshot.jpg",
            mimeType: String =  "image/jpeg",
            directory: String = Environment.DIRECTORY_PICTURES,
            mediaContentUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        ) {
            val imageOutStream: OutputStream
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                    put(MediaStore.Images.Media.MIME_TYPE, mimeType)
                    put(MediaStore.Images.Media.RELATIVE_PATH, directory)
                }

                context.contentResolver.run {
                    val uri =
                        context.contentResolver.insert(mediaContentUri, values)
                            ?: return
                    imageOutStream = openOutputStream(uri) ?: return
                }
            } else {
                val imagePath = context.getExternalFilesDir(directory)?.absolutePath
                val image = File(imagePath, filename)
                imageOutStream = FileOutputStream(image)
            }

            imageOutStream.use {
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, it)
            }
        }



    }

}
