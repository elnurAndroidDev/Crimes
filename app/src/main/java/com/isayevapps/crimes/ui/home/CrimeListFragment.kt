package com.isayevapps.crimes.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.isayevapps.crimes.R
import com.isayevapps.crimes.adapters.CrimeListAdapter
import com.isayevapps.crimes.databinding.FragmentCrimeListBinding
import com.isayevapps.crimes.models.Crime
import kotlinx.coroutines.launch

class CrimeListFragment : Fragment() {


    private var _binding: FragmentCrimeListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Binding is null"
        }

    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProvider(requireActivity())[CrimeListViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCrimeListBinding.inflate(layoutInflater, container, false)
        binding.crimesRV.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                crimeListViewModel.crimes.collect { crimes ->
                    updateUI(crimes)
                }
            }
        }
    }

    private fun setupMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.fragment_crime_list, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.add_crime -> {
                        showNewCrime()
                        true
                    }
                    else -> true
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun showNewCrime() {
        viewLifecycleOwner.lifecycleScope.launch {
            val newCrime = Crime()
            crimeListViewModel.addCrime(newCrime)
            findNavController().navigate(
                CrimeListFragmentDirections.showCrimeDetail(newCrime.id)
            )
        }
    }

    private fun updateUI(crimes: List<Crime> = emptyList()) {
        val crimeListAdapter = CrimeListAdapter(crimes) { crimeID ->
            findNavController().navigate(
                CrimeListFragmentDirections.showCrimeDetail(crimeID)
            )
        }
        binding.crimesRV.adapter = crimeListAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}