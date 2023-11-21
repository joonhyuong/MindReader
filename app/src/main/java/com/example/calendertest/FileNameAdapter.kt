package com.example.calendertest

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FileNameAdapter(private val fileDataList: List<Pair<String, String>>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<FileNameAdapter.FileNameViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(fileName: String, fileContent: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileNameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.file_name_item, parent, false)
        return FileNameViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileNameViewHolder, position: Int) {
        val (fileName, fileContent) = fileDataList[position]
        holder.bind(fileName, fileContent)
    }

    override fun getItemCount(): Int {
        return fileDataList.size
    }

    inner class FileNameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val fileNameTextView: TextView = itemView.findViewById(R.id.fileNameTextView)
        private val fileInfoTextView: TextView = itemView.findViewById(R.id.fileInfoTextView)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(fileName: String, fileContent: String) {
            fileNameTextView.text = fileName
            fileInfoTextView.text = fileContent
        }

        override fun onClick(view: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val (fileName, fileContent) = fileDataList[position]
                listener.onItemClick(fileName, fileContent)
            }
        }
    }
}