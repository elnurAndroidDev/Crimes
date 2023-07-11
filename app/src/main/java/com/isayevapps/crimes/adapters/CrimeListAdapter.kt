package com.isayevapps.crimes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.isayevapps.crimes.R
import com.isayevapps.crimes.models.Crime

class CrimeListAdapter(
    private val crimes: List<Crime>,
    private val clicker: OnCrimeClicked
) : RecyclerView.Adapter<CrimeListAdapter.CrimeHolder>() {

    inner class CrimeHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var crime: Crime

        private val titleTextView = itemView.findViewById<TextView>(R.id.crime_title)
        private val dateTextView = itemView.findViewById<TextView>(R.id.crime_date)
        private val solvedImageView = itemView.findViewById<ImageView>(R.id.solvedImageView)

        init {
            itemView.setOnClickListener {
                clicker.onCrimeClick(crime.id)
            }
        }

        fun bind(crime: Crime) {
            this.crime = crime
            titleTextView.text = crime.title
            dateTextView.text = crime.date.toString()
            solvedImageView.visibility = if (crime.isSolved) View.VISIBLE else View.GONE
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