package com.isayevapps.crimes.ui.dialogs

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import java.util.Calendar
import java.util.GregorianCalendar

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private val args: TimePickerFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        c.time = args.crimeTime
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        return TimePickerDialog(
            requireContext(),
            this,
            hour,
            minute,
            DateFormat.is24HourFormat(activity)
        )
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        val c = Calendar.getInstance()
        c.time = args.crimeTime
        c.set(
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH),
            hourOfDay,
            minute
        )
        val resultTime = c.time
        setFragmentResult(
            REQUEST_KEY_DATE,
            bundleOf(BUNDLE_KEY_DATE to resultTime)
        )
    }

    companion object {
        const val REQUEST_KEY_DATE = "REQUEST_KEY_TIME"
        const val BUNDLE_KEY_DATE = "BUNDLE_KEY_TIME"
    }
}