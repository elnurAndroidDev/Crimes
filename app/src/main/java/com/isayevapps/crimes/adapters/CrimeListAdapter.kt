package com.isayevapps.crimes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.isayevapps.crimes.databinding.ListItemCrimeBinding
import com.isayevapps.crimes.models.Crime
import com.isayevapps.crimes.utils.DateUtils
import java.util.UUID

class CrimeListAdapter(
    private val crimes: List<Crime>,
    private val onCrimeClicked: (crimeID: UUID) -> Unit
) : RecyclerView.Adapter<CrimeListAdapter.CrimeHolder>() {

    inner class CrimeHolder(private val binding: ListItemCrimeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var crime: Crime

        fun bind(crime: Crime, onCrimeClicked: (crimeID: UUID) -> Unit) {
            this.crime = crime
            binding.crimeTitle.text = crime.title
            binding.crimeDate.text = DateUtils.getDateAndTimeString(crime.date)
            binding.solvedImageView.visibility = if (crime.solved) View.VISIBLE else View.GONE
            binding.root.setOnClickListener {
                onCrimeClicked(crime.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemCrimeBinding.inflate(inflater, parent, false)
        return CrimeHolder(binding)
    }

    override fun getItemCount() = crimes.size

    override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
        val crime = crimes[position]
        holder.bind(crime, onCrimeClicked)
    }
}