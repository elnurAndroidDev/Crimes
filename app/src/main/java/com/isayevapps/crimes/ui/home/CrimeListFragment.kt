package com.isayevapps.crimes.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.isayevapps.crimes.adapters.CrimeListAdapter
import com.isayevapps.crimes.adapters.OnCrimeClicked
import com.isayevapps.crimes.databinding.FragmentCrimeListBinding
import com.isayevapps.crimes.models.Crime
import kotlinx.coroutines.launch

class CrimeListFragment : Fragment() {


    private var _binding: FragmentCrimeListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Binding is null"
        }

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
        _binding = FragmentCrimeListBinding.inflate(layoutInflater, container, false)
        binding.crimesRV.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                crimeListViewModel.crimes.collect { crimes ->
                    updateUI(crimes)
                }
            }
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

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }

}