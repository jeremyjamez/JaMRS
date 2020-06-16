/*
 * *
 *  * Created by Jeremy James on 6/16/20 10:35 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 6/13/20 10:32 PM
 *
 */

package xyz.james.home.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Config
import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.paging.toLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.james.db.entities.Appointment
import xyz.james.db.entities.Patient
import xyz.james.db.repositories.AppointmentRepository
import xyz.james.db.repositories.PatientRepository
import xyz.james.shared.utils.SingleLiveEvent

class AppointmentViewModel(val patientRepository: PatientRepository, val appointmentRepository: AppointmentRepository) : ViewModel() {

    val appointmentDate : MutableLiveData<String> = MutableLiveData()
    val appointmentTime : MutableLiveData<String> = MutableLiveData()
    val appointmentPatientId : MutableLiveData<String> = MutableLiveData()
    val appointmentNote : MutableLiveData<String> = MutableLiveData()

    val myPagingConfig = Config(
        pageSize = 50,
        prefetchDistance = 150,
        enablePlaceholders = true
    )

    private val isAppointmentCreated: SingleLiveEvent<Boolean> = SingleLiveEvent()

    fun isAppointmentCreated(): SingleLiveEvent<Boolean> {
        return isAppointmentCreated
    }

    fun findPatientById(id: String) : LiveData<PagedList<Patient>> {
        val patientDataSource : DataSource.Factory<Int, Patient> = patientRepository.findPatientById(id)
        return patientDataSource.toLiveData(myPagingConfig)
    }

    fun save() {
        if (appointmentDate.value.isNullOrBlank() && appointmentTime.value.isNullOrBlank() && appointmentPatientId.value.isNullOrBlank() && appointmentNote.value.isNullOrBlank()){

        } else {
            val appointment = Appointment(appointmentPatientId.value!!, appointmentDate.value!!, appointmentTime.value!!, appointmentNote.value!!)
            appointment.getAppointmentId()
            viewModelScope.launch(Dispatchers.IO) {
                val row = appointmentRepository.insert(appointment)
                isAppointmentCreated.postValue(row > 0)
            }
        }
    }
}