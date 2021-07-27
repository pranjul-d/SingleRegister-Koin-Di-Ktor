package com.softradix.singlekoin.koinKtorExample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.softradix.singlekoin.R
import kotlinx.android.synthetic.main.item_list.view.*

class AttachmentAdapter(private var dummies: Array<DummyData>, val listener: (DummyData) -> Unit) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false))

    override fun getItemCount(): Int = dummies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dummy = dummies[position]
        with(holder.itemView) {
            title.text = dummy.title
            downloadIcon.isVisible = !dummy.file.exists()
            documentTypeIcon.isVisible = dummy.file.exists()
            progressBarDocument.isVisible = dummy.isDownloading
            setOnClickListener {
                listener(dummy)
            }
        }
    }

    fun setDownloading(dummy: DummyData, isDownloading: Boolean) {
        getDummy(dummy)?.isDownloading = isDownloading
        notifyItemChanged(dummies.indexOf(dummy))
    }

    fun setProgress(dummy: DummyData, progress: Int) {
        getDummy(dummy)?.progress = progress
        notifyItemChanged(dummies.indexOf(dummy), Bundle().apply { putInt("progress", progress) })
    }

    private fun getDummy(dummy: DummyData) = dummies.find { dummy.id == it.id }

}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view)