package com.isayevapps.crimes.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.isayevapps.crimes.adapters.CrimeListAdapter
import com.isayevapps.crimes.adapters.OnCrimeClicked
import com.isayevapps.crimes.databinding.FragmentCrimeListBinding
import com.isayevapps.crimes.models.Crime

class CrimeListFragment : Fragment() {


    private lateinit var binding: FragmentCrimeListBinding
    private var callback: OnCrimeClicked? = null

    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProvider(requireActivity())[CrimeListViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as OnCrimeClicked
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCrimeListBinding.inflate(layoutInflater, container, false)
        binding.crimesRV.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeListViewModel.crimeListLiveData.observe(viewLifecycleOwner) { crimes ->
            crimes?.let {
                updateUI(crimes)
            }
        }
    }

    private fun updateUI(crimes: List<Crime> = emptyList()) {
        val crimeListAdapter = CrimeListAdapter(crimes, callback!!)
        binding.crimesRV.adapter = crimeListAdapter
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }

}