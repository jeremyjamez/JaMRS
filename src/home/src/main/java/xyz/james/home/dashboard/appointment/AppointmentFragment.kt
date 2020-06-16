/*
 * *
 *  * Created by Jeremy James on 6/16/20 10:35 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 6/13/20 10:11 PM
 *
 */

package xyz.james.home.dashboard.appointment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.michalsvec.singlerowcalendar.utils.DateUtils
import kotlinx.android.synthetic.main.calendar_item.view.*
import kotlinx.android.synthetic.main.fragment_appointment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import xyz.james.home.R
import xyz.james.home.databinding.FragmentAppointmentBinding
import xyz.james.home.viewmodels.AppointmentViewModel
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class AppointmentFragment : Fragment() {

    private val calendar = Calendar.getInstance()
    private var currentMonth = 0

    val appointmentViewModel : AppointmentViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(fabSchedule.isExpanded)
                    fabSchedule.isExpanded = !fabSchedule.isExpanded
                else
                    findNavController().popBackStack()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentAppointmentBinding = FragmentAppointmentBinding.inflate(inflater, container, false)
        binding.vm = appointmentViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appointmentViewModel.appointmentPatientId.observe(viewLifecycleOwner, Observer { patientId ->
            val list = appointmentViewModel.findPatientById(patientId)
            list.observe(viewLifecycleOwner, Observer {
                for (patient in it) {
                    val idList = listOf(patient.getPatientId())
                    val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_patient_item, idList)
                    dropDownAppointmentPatient.setAdapter(adapter)
                }
            })
        })

        appointmentViewModel.isAppointmentCreated().observe(viewLifecycleOwner, Observer {
            if (it)
                Snackbar.make(requireView(), "Appointment has been added!", Snackbar.LENGTH_LONG).show()
            else
                Snackbar.make(requireView(), "Failed to add appointment!", Snackbar.LENGTH_LONG).show()
        })

        calendar.time = Date()
        currentMonth = calendar[Calendar.MONTH]

        val myCalendarViewManager = object : CalendarViewManager {
            override fun setCalendarViewResourceId(
                position: Int,
                date: Date,
                isSelected: Boolean
            ): Int {
                // set date to calendar according to position where we are
                //val cal = Calendar.getInstance()
                //cal.time = date
                // if item is selected we return this layout items
                return if (isSelected)
                    R.layout.selected_calendar_item
                else
                // here we return items which are not selected
                    R.layout.calendar_item
            }

            override fun bindDataToCalendarView(
                holder: SingleRowCalendarAdapter.CalendarViewHolder,
                date: Date,
                position: Int,
                isSelected: Boolean
            ) {
                holder.itemView.tv_date_calendar_item.text = DateUtils.getDayNumber(date)
                holder.itemView.tv_day_calendar_item.text = DateUtils.getDay3LettersName(date)
            }
        }

        // using calendar changes observer we can track changes in calendar
        val myCalendarChangesObserver = object :
            CalendarChangesObserver {
            // you can override more methods, in this example we need only this one
            override fun whenSelectionChanged(isSelected: Boolean, position: Int, date: Date) {
                txtDate.text = "${DateUtils.getMonthName(date)} ${DateUtils.getDayNumber(date)}, ${DateUtils.getDayName(date)}"
                super.whenSelectionChanged(isSelected, position, date)
            }
        }

        // selection manager is responsible for managing selection
        val mySelectionManager = object : CalendarSelectionManager {
            override fun canBeItemSelected(position: Int, date: Date): Boolean {
                // set date to calendar according to position
                val cal = Calendar.getInstance()
                cal.time = date
                // in this example sunday and saturday can't be selected, others can
                return true
            }
        }

        // here we init our calendar, also you can set more properties if you haven't specified in XML layout
        val singleRowCalendar = main_single_row_calendar.apply {
            calendarViewManager = myCalendarViewManager
            calendarChangesObserver = myCalendarChangesObserver
            calendarSelectionManager = mySelectionManager
            setDates(getFutureDatesOfCurrentMonth())
            init()
        }

        val cal = Calendar.getInstance()
        cal.time = Date()

        singleRowCalendar.select(cal[Calendar.DATE]-1)
        singleRowCalendar.scrollToPosition(cal[Calendar.DATE]-2)

        btnNext.setOnClickListener{
            singleRowCalendar.setDates(getDatesOfNextMonth())
        }

        btnPrev.setOnClickListener {
            singleRowCalendar.setDates(getDatesOfPreviousMonth())
        }

        fabSchedule.setOnClickListener {
            fabSchedule.isExpanded = !fabSchedule.isExpanded
        }

        btnSaveAppointment.setOnClickListener {
            fabSchedule.isExpanded = !fabSchedule.isExpanded
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            if (fabSchedule.isExpanded)
                fabSchedule.isExpanded = !fabSchedule.isExpanded
        }
        return true
    }

    private fun getDatesOfNextMonth(): List<Date> {
        currentMonth++ // + because we want next month
        if (currentMonth == 12) {
            // we will switch to january of next year, when we reach last month of year
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] + 1)
            currentMonth = 0 // 0 == january
        }
        return getDates(mutableListOf())
    }

    private fun getDatesOfPreviousMonth(): List<Date> {
        currentMonth-- // - because we want previous month
        if (currentMonth == -1) {
            // we will switch to december of previous year, when we reach first month of year
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] - 1)
            currentMonth = 11 // 11 == december
        }
        return getDates(mutableListOf())
    }

    private fun getFutureDatesOfCurrentMonth(): List<Date> {
        // get all next dates of current month
        currentMonth = calendar[Calendar.MONTH]
        return getDates(mutableListOf())
    }

    private fun getDates(list: MutableList<Date>): List<Date> {
        // load dates of whole month
        calendar.set(Calendar.MONTH, currentMonth)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        list.add(calendar.time)
        while (currentMonth == calendar[Calendar.MONTH]) {
            calendar.add(Calendar.DATE, +1)
            if (calendar[Calendar.MONTH] == currentMonth)
                list.add(calendar.time)
        }
        calendar.add(Calendar.DATE, -1)
        return list
    }

}
