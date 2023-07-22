package com.isayevapps.crimes.ui.details

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.MenuProvider
import androidx.core.view.doOnLayout
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.isayevapps.crimes.R
import com.isayevapps.crimes.databinding.FragmentCrimeBinding
import com.isayevapps.crimes.models.Crime
import com.isayevapps.crimes.ui.dialogs.DateDialogFragment
import com.isayevapps.crimes.ui.dialogs.TimePickerFragment
import com.isayevapps.crimes.utils.DateUtils
import com.isayevapps.crimes.utils.PictureUtils
import kotlinx.coroutines.launch
import java.io.File
import java.io.Serializable
import java.util.Date

private const val DATE_FORMAT = "EEE, MMM, dd"
class CrimeFragment : Fragment() {
    private var _binding: FragmentCrimeBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Binding is null"
        }

    private val args: CrimeFragmentArgs by navArgs()

    private val crimeDetailViewModel: CrimeDetailViewModel by viewModels {
        CrimeDetailViewModelFactory(args.crimeID)
    }

    private val selectSuspect = registerForActivityResult(
        ActivityResultContracts.PickContact()
    ) { uri: Uri? ->
        uri?.let { parseContactSelection(it) }
    }

    private var photoName: String? = null
    private val takePhoto = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { didTakePhoto: Boolean ->
        if (didTakePhoto && photoName != null) {
            crimeDetailViewModel.updateCrime { oldCrime ->
                oldCrime.copy(photoFileName = photoName)
            }
        }
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
        setupMenu()
        binding.apply {
            crimeTitle.doOnTextChanged { text, _, _, _ ->
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(title = text.toString())
                }
            }

            crimeSolved.setOnCheckedChangeListener { _, isChecked ->
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(solved = isChecked)
                }
            }
            val selectSuspectIntent = selectSuspect.contract.createIntent(
                requireContext(),
                null
            )
            chooseSuspectBtn.isEnabled = canResolveIntent(selectSuspectIntent)
            chooseSuspectBtn.setOnClickListener {
                selectSuspect.launch(null)
            }

            val captureImageIntent = takePhoto.contract.createIntent(
                requireContext(),
                Uri.parse("")
            )
            crimeCamera.isEnabled = canResolveIntent(captureImageIntent)

            crimeCamera.setOnClickListener {
                photoName = "IMG_${Date()}.JPG"
                val photoFile = File(requireContext().applicationContext.filesDir, photoName!!)
                val photoUri =
                    FileProvider.getUriForFile(requireContext(), "com.isayevapps.crimes", photoFile)
                takePhoto.launch(photoUri)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                crimeDetailViewModel.crime.collect { crime ->
                    crime?.let { updateUI(it) }
                }
            }
        }

        setFragmentResultListener(DateDialogFragment.REQUEST_KEY_DATE) { _, bundle ->
            val newDate = bundle.customGetSerializable<Date>(DateDialogFragment.BUNDLE_KEY_DATE)
            newDate?.let {
                crimeDetailViewModel.updateCrime { it.copy(date = newDate) }
            }
        }

        setFragmentResultListener(TimePickerFragment.REQUEST_KEY_DATE) { _, bundle ->
            val newDate = bundle.customGetSerializable<Date>(TimePickerFragment.BUNDLE_KEY_DATE)
            newDate?.let {
                crimeDetailViewModel.updateCrime { it.copy(date = newDate) }
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (binding.crimeTitle.text.isEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.enter_title),
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.crimeTitle.requestFocus()
                    } else {
                        isEnabled = false
                        activity?.onBackPressed()
                    }
                }
            })
    }


    @Suppress("DEPRECATION")
    private inline fun <reified T : Serializable> Bundle.customGetSerializable(key: String): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getSerializable(key, T::class.java)
        } else {
            getSerializable(key) as? T
        }
    }

    private fun setupMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.fragment_crime_menu, menu)
                val saveUpdateItem = menu.findItem(R.id.save_update_crime)
                if (args.crimeID == NEW_CRIME)
                    saveUpdateItem.setTitle(R.string.save_crime)
                else
                    saveUpdateItem.setTitle(R.string.update_crime)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.delete_crime -> {
                        crimeDetailViewModel.deleteCrime()
                        findNavController().popBackStack()
                        true
                    }

                    R.id.save_update_crime -> {
                        crimeDetailViewModel.saveOrUpdate()
                        findNavController().popBackStack()
                        true
                    }

                    else -> true
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun updateUI(crime: Crime) {
        binding.apply {
            if (crimeTitle.text.toString() != crime.title)
                crimeTitle.setText(crime.title)
            crimeDate.text = DateUtils.getDateString(crime.date)
            crimeTime.text = DateUtils.getTimeString(crime.date)
            crimeDate.setOnClickListener {
                findNavController().navigate(CrimeFragmentDirections.selectDate(crime.date))
            }
            crimeTime.setOnClickListener {
                findNavController().navigate(CrimeFragmentDirections.selectTime(crime.date))
            }
            crimeSolved.isChecked = crime.solved

            updatePhoto(crime.photoFileName)

            chooseSuspectBtn.text = crime.suspect.ifEmpty {
                getString(R.string.choose_suspect_txt)
            }

            sendReportBtn.setOnClickListener {
                val reportIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, getCrimeReport(crime))
                    putExtra(
                        Intent.EXTRA_SUBJECT,
                        getString(R.string.crime_report_subject)
                    )
                }
                val chooserIntent =
                    Intent.createChooser(reportIntent, getString(R.string.send_report))
                startActivity(chooserIntent)
            }
        }
    }

    private fun updatePhoto(photoFileName: String?) {
        if (binding.crimePhoto.tag != photoFileName) {
            val photoFile = photoFileName?.let {
                File(requireContext().applicationContext.filesDir, it)
            }

            if (photoFile?.exists() == true) {
                binding.crimePhoto.doOnLayout { measuredView ->
                    val scaledBitmap = PictureUtils.getScaledBitmap(
                        photoFile.path,
                        measuredView.width,
                        measuredView.height
                    )
                    binding.crimePhoto.setImageBitmap(scaledBitmap)
                    binding.crimePhoto.tag = photoFileName
                }
            } else {
                binding.crimePhoto.setImageBitmap(null)
                binding.crimePhoto.tag = null
            }
        }
    }


    private fun getCrimeReport(crime: Crime): String {
        val solvedString = if (crime.solved) {
            getString(R.string.crime_report_solved)
        } else {
            getString(R.string.crime_report_unsolved)
        }

        val dateString = DateFormat.format(DATE_FORMAT, crime.date).toString()
        val suspectText = if (crime.suspect.isBlank()) {
            getString(R.string.crime_report_no_suspect)
        } else {
            getString(R.string.crime_report_suspect, crime.suspect)
        }

        return getString(
            R.string.crime_report,
            crime.title, dateString, solvedString, suspectText
        )
    }

    private fun parseContactSelection(contactUri: Uri) {
        val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)

        val queryCursor = requireActivity().contentResolver
            .query(contactUri, queryFields, null, null, null)

        queryCursor?.use { cursor ->
            if (cursor.moveToFirst()) {
                val suspect = cursor.getString(0)
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(suspect = suspect)
                }
            }
        }
    }

    private fun canResolveIntent(intent: Intent): Boolean {
        val packageManager: PackageManager = requireActivity().packageManager
        val resolvedActivity: ResolveInfo? =
            packageManager.resolveActivity(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY
            )
        return resolvedActivity != null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val NEW_CRIME = "new crime"
    }
}