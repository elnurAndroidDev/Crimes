package com.isayevapps.crimes.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.isayevapps.crimes.databinding.FragmentCrimeBinding
import com.isayevapps.crimes.models.Crime

private const val ARG_CRIME_ID = "crime_id"

class CrimeFragment : Fragment() {

    private lateinit var crime: Crime
    private var _binding: FragmentCrimeBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Binding is null"
        }

    private val args: CrimeFragmentArgs by navArgs()

    private val crimeDetailViewModel: CrimeDetailViewModel by lazy {
        ViewModelProvider(requireActivity())[CrimeDetailViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*val crimeId = arguments?.getInt(ARG_CRIME_ID)
        crimeDetailViewModel.loadCrime(crimeId!!)*/
        crime = Crime(title = "Murder")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCrimeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
        binding.apply {
            crimeTitle.doOnTextChanged { text, _, _, _ ->
                crime = crime.copy(title = text.toString())
            }

            crimeDate.apply {
                text = crime.date.toString()
                isEnabled = false
            }

            crimeSolved.setOnCheckedChangeListener { _, isChecked ->
                crime = crime.copy(isSolved = isChecked)
            }
        }
        crimeDetailViewModel.crimeLiveData.observe(viewLifecycleOwner) { crime ->
            crime?.let {
                this.crime = crime
                updateUI()
            }
        }
    }

    private fun updateUI() {
        binding.crimeTitle.setText(crime.title)
        binding.crimeDate.text = crime.date.toString()
        binding.crimeSolved.isChecked = crime.isSolved
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(crimeId: Int): CrimeFragment {
            val args = Bundle().apply {
                putSerializable(ARG_CRIME_ID, crimeId)
            }
            return CrimeFragment().apply {
                arguments = args
            }
        }
    }

}