package com.softradix.singlekoin.koinKtorExample

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import com.softradix.singlekoin.BuildConfig
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import java.io.File
import kotlin.math.roundToInt

val globalContext: Context
    get() = GlobalContext.get().koin.rootScope.androidContext()

@KtorExperimentalAPI
fun initKtorClient() = HttpClient(Android) {
    install(Logging) {
        logger = Logger.ANDROID
        level = LogLevel.ALL
    }
}

fun Activity.openFile(file: File) {
    Intent(Intent.ACTION_VIEW).apply {
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        addCategory(Intent.CATEGORY_DEFAULT)
        val uri = FileProvider.getUriForFile(this@openFile, BuildConfig.APPLICATION_ID + ".provider", file)
        val mimeType = getMimeType(file)
        mimeType?.let {
            setDataAndType(uri, it)
            startActivity(this)
        }

    }
}

fun getMimeType(file: File): String? {
    val extension = file.extension
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
}

suspend fun HttpClient.downloadFile(file: File, url: String): Flow<DownloadResult> {
    return flow {
        val response = request<HttpResponse> {
            url(url)
            method = HttpMethod.Get
        }
        val data = ByteArray(response.contentLength()!!.toInt())
        var offset = 0
        do {
            val currentRead = response.content.readAvailable(data, offset, data.size)
            offset += currentRead
            val progress = (offset * 100f / data.size).roundToInt()
            emit(DownloadResult.Progress(progress))
        } while (currentRead > 0)
        response.close()
        if (response.status.isSuccess()) {
            file.writeBytes(data)
            emit(DownloadResult.Success)
        } else {
            emit(DownloadResult.Error("File not downloaded"))
        }
    }
}



suspend fun HttpClient.downloadFile(
    file: File,
    webUrl: String,
    callback: suspend (boolean: Boolean) -> Unit
) {
    val call = this.request<HttpResponse> {
        url { webUrl }
        method = HttpMethod.Get
    }
    if (!call.status.isSuccess()) {
        callback(false)
    }
    call.content.copyAndClose(file.writeChannel())
    return callback(true)
}