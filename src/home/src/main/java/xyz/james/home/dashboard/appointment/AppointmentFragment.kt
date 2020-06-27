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
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView
import com.michalsvec.singlerowcalendar.calendar.CalendarChangesObserver
import com.michalsvec.singlerowcalendar.calendar.CalendarViewManager
import com.michalsvec.singlerowcalendar.calendar.SingleRowCalendarAdapter
import com.michalsvec.singlerowcalendar.selection.CalendarSelectionManager
import com.michalsvec.singlerowcalendar.utils.DateUtils
import kotlinx.android.synthetic.main.calendar_item.view.*
import kotlinx.android.synthetic.main.fragment_appointment.*
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import xyz.james.db.entities.Appointment
import xyz.james.db.repositories.PatientRepository
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
    private lateinit var appointmentPagedList : PagedList<Appointment>

    val appointmentViewModel : AppointmentViewModel by viewModel()
    val patientRepository : PatientRepository by inject()

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

        appointmentViewModel.findAllAppointments().observe(viewLifecycleOwner, Observer { appointmentList ->
            if (appointmentList != null && appointmentList.size > 0){
                val adapter = AppointmentAdapter(appointmentList, patientRepository)
                appointmentRecyclerView.adapter = adapter
                emptyAppointmentLayoutState.visibility = View.GONE
                appointmentRecyclerView.visibility = View.VISIBLE
            } else {
                emptyAppointmentLayoutState.visibility = View.VISIBLE
                appointmentRecyclerView.visibility = View.GONE
            }
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
                val localDate = LocalDate.parse("${DateUtils.getDayNumber(date)}/${DateUtils.getMonthNumber(date)}/${DateUtils.getYear(date)}", DateTimeFormatter.ofPattern("d/M/yyyy"))
                return if (isSelected)
                    R.layout.selected_calendar_item
                else
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
                super.whenSelectionChanged(isSelected, position, date)
                txtDate.text = "${DateUtils.getMonthName(date)} ${DateUtils.getDayNumber(date)}, ${DateUtils.getDay3LettersName(date)}, ${DateUtils.getYear(date)}"
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

    private class AppointmentAdapter(val appointmentList: PagedList<Appointment>, val patientRepository: PatientRepository) : RecyclerView.Adapter<AppointmentViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
            return AppointmentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.appointment_item, parent, false))
        }

        override fun getItemCount(): Int {
            return appointmentList.size
        }

        override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
            val appointment = appointmentList[position]
            runBlocking {
                val patient = patientRepository.findPatientById(appointment?.patientId!!)
                holder.txtPatientName.text = patient.patientFullNameWithMiddleInitial() ?: ""
            }

            holder.txtPatientId.text = appointment?.patientId
            holder.txtDoctorsNote.text = appointment?.appointmentNote
            holder.btnAppointmentDate.text = appointment?.appointmentDate.toString()
            holder.btnAppointmentTime.text = appointment?.appointmentTime?.format(DateTimeFormatter.ofPattern("hh:mm a"))
        }
    }

    private class AppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val txtPatientName = itemView.findViewById<MaterialTextView>(R.id.txtPatientName)
        val txtPatientId = itemView.findViewById<MaterialTextView>(R.id.txtPatientId)
        val txtDoctorsNote = itemView.findViewById<MaterialTextView>(R.id.txtDoctorsNote)
        val btnAppointmentDate = itemView.findViewById<MaterialButton>(R.id.btnAppointmentDate)
        val btnAppointmentTime = itemView.findViewById<MaterialButton>(R.id.btnAppointmentTime)
    }
}
