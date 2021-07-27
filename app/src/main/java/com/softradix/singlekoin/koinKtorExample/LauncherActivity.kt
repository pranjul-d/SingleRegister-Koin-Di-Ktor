package com.softradix.singlekoin.koinKtorExample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.softradix.singlekoin.koinKtorExample.*
import com.softradix.singlekoin.R
import io.ktor.client.*
import kotlinx.android.synthetic.main.activity_launcher.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject

@InternalCoroutinesApi
class LauncherActivity : AppCompatActivity() {
    companion object {
        val data = arrayOf(
            DummyData("1", "Title1.pdf", "https://css4.pub/2015/icelandic/dictionary.pdf"),
            DummyData("2", "Title2.pdf", "https://css4.pub/2015/icelandic/dictionary.pdf"),
            DummyData("3", "Title3.pdf", "https://css4.pub/2015/icelandic/dictionary.pdf"),
            DummyData("4", "Title4.pdf", "https://css4.pub/2017/newsletter/drylab.pdf"),
            DummyData("5", "Title5.pdf", "https://css4.pub/2017/newsletter/drylab.pdf"),
            DummyData("6", "Title6.pdf", "https://css4.pub/2017/newsletter/drylab.pdf")
        )
    }

    lateinit var myAdapter: AttachmentAdapter
    val ktor: HttpClient by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
        with(recyclerView) {
            layoutManager = LinearLayoutManager(this@LauncherActivity)
            DividerItemDecoration(
                context,
                (layoutManager as LinearLayoutManager).orientation
            ).apply {
                addItemDecoration(this)
            }
            myAdapter = AttachmentAdapter(data) { dummy ->
                manageClickAdapter(dummy)
            }
            adapter = myAdapter
        }
    }


    fun manageClickAdapter(dummy: DummyData) {
        when {
            dummy.isDownloading -> {
                //Do nothing
            }
            dummy.file.exists() -> openFile(dummy.file)
            else -> {
                try {
                    downloadWithFlow(dummy)
                } catch (e: Exception) {
                    Log.e("TAG", "manageClickAdapter: ${e.localizedMessage}" )
                    //generic error while downloading
                }
            }
        }
    }



    @InternalCoroutinesApi
    private fun downloadWithFlow(dummy: DummyData) {
        CoroutineScope(Dispatchers.IO).launch {
            ktor.downloadFile(dummy.file, dummy.url).collect{
                withContext(Dispatchers.Main) {
                when (it) {
                    is DownloadResult.Success -> {
                        myAdapter.setDownloading(dummy, false)
                    }
                    is DownloadResult.Error -> {
                        myAdapter.setDownloading(dummy, false)
                        Toast.makeText(this@LauncherActivity, "Error while downloading ${dummy.title}", Toast.LENGTH_LONG).show()
                    }
                    is DownloadResult.Progress -> {
                        myAdapter.setProgress(dummy, it.progress)
                    }
                }
            }}
    }

}
}