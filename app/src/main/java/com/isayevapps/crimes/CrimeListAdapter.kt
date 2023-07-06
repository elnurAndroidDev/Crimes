package com.isayevapps.crimes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CrimeListAdapter(
    private val crimes: List<Crime>
) : RecyclerView.Adapter<CrimeListAdapter.CrimeHolder>() {

    inner class CrimeHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleTextView = itemView.findViewById<TextView>(R.id.crime_title)
        private val dateTextView = itemView.findViewById<TextView>(R.id.crime_date)

        fun bind(crime: Crime) {
            titleTextView.text = crime.title
            dateTextView.text = crime.date.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_crime, parent, false)
        return CrimeHolder(view)
    }

    override fun getItemCount() = crimes.size

    override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
        val crime = crimes[position]
        holder.bind(crime)
    }
}