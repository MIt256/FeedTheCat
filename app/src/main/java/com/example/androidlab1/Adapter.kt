package com.example.androidlab1

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androidlab1.databinding.DialogRecordsBinding

class Adapter() :RecyclerView.Adapter<Adapter.ContactHolder>() {

    lateinit var recordsList:MutableList<Pair>

    class ContactHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = DialogRecordsBinding.bind(view)
        fun bind(cont: Pair) = with(binding) {
                record.text = cont.points.toString()
                date.text = cont.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dialog_records, parent, false)
        return ContactHolder(view)
    }

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.bind(recordsList[position])
    }

    override fun getItemCount(): Int {
        return recordsList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addToList(items: MutableList<Pair>) {
        recordsList = items
        notifyDataSetChanged()
    }

}