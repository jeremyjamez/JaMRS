/*
 * *
 *  * Created by Jeremy James on 6/16/20 10:35 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 6/10/20 9:05 AM
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
import kotlinx.coroutines.withContext
import xyz.james.db.entities.Address
import xyz.james.db.entities.Patient
import xyz.james.db.repositories.PatientRepository
import xyz.james.home.R
import xyz.james.shared.utils.SingleLiveEvent

class PatientViewModel(val patientRepository: PatientRepository) : ViewModel() {

    private val inserted: SingleLiveEvent<Boolean> = SingleLiveEvent()

    fun getInserted(): SingleLiveEvent<Boolean>{
        return inserted
    }

    val firstName: MutableLiveData<String> = MutableLiveData<String>()

    val middleName: MutableLiveData<String> = MutableLiveData<String>()

    val lastName: MutableLiveData<String> = MutableLiveData<String>()

    val dob: MutableLiveData<String> = MutableLiveData<String>()

    private val _gender: MutableLiveData<String> = MutableLiveData<String>()
    val gender: MutableLiveData<Int> = MutableLiveData<Int>()

    val addressLine1: MutableLiveData<String> = MutableLiveData<String>()

    val addressLine2: MutableLiveData<String> = MutableLiveData<String>()

    val addressParish: MutableLiveData<String> = MutableLiveData<String>()

    private val _medicalHistoryList = ArrayList<String>()
    private val _familyMedicalHistoryList = ArrayList<String>()

    val chkMedicalType1Diabetes: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val chkMedicalType2Diabetes: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val chkMedicalHypertension: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val chkMedicalHeartDisease: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    val chkFamilyType1Diabetes: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val chkFamilyType2Diabetes: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val chkFamilyHypertension: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val chkFamilyHeartDisease: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    val medicalOther: MutableLiveData<String> = MutableLiveData<String>()
    val familyOther: MutableLiveData<String> = MutableLiveData<String>()

    val myPagingConfig = Config(
        pageSize = 50,
        prefetchDistance = 150,
        enablePlaceholders = true
    )

    fun addPatientClick(){
        if (chkMedicalType1Diabetes.value == true) _medicalHistoryList.add("Type 1 Diabetes")
        if (chkMedicalType2Diabetes.value == true) _medicalHistoryList.add("Type 2 Diabetes")
        if(chkMedicalHeartDisease.value == true) _medicalHistoryList.add("Heart Disease")
        if(chkMedicalHypertension.value == true) _medicalHistoryList.add("Hypertension")

        if (chkFamilyType1Diabetes.value == true) _familyMedicalHistoryList.add("Type 1 Diabetes")
        if (chkFamilyType2Diabetes.value == true) _familyMedicalHistoryList.add("Type 2 Diabetes")
        if(chkFamilyHeartDisease.value == true) _familyMedicalHistoryList.add("Heart Disease")
        if(chkFamilyHypertension.value == true) _familyMedicalHistoryList.add("Hypertension")

        if (gender.value == R.id.chk_gender_male)
            _gender.value = "Male"
        else if (gender.value == R.id.chk_gender_female)
            _gender.value = "Female"

        if(!addressLine1.value.isNullOrBlank() && !addressLine2.value.isNullOrBlank() && !addressParish.value.isNullOrBlank()){
            val address = Address(addressLine1.value!!, addressLine2.value!!, addressParish.value!!)
            if(firstName.value == null && middleName.value == null && lastName.value == null && dob.value == null && _gender.value == null){

            } else {
                viewModelScope.launch {
                    val lastId = findLastPatient()
                    val patient = Patient(firstName.value!!, middleName.value!!, lastName.value!!, address, dob.value!!, _gender.value!!, _medicalHistoryList, _familyMedicalHistoryList)
                    patient.generatePatientId(lastId)

                    viewModelScope.launch {
                        val result = performInsert(patient)
                        inserted.value = result > 0
                    }
                }
            }
        }
    }

    fun findPatientById(id: String) : LiveData<PagedList<Patient>> {
        val patientDataSource : DataSource.Factory<Int, Patient> = patientRepository.findPatientById(id)
        return patientDataSource.toLiveData(myPagingConfig)
    }

    fun getAllPatients() : LiveData<PagedList<Patient>> {
        val patientDataSource : DataSource.Factory<Int, Patient> = patientRepository.getAllPatients()
        return patientDataSource.toLiveData(myPagingConfig)
    }

    fun findPatientByFilters(firstName: String, middleName: String, lastName: String, gender: String) : LiveData<PagedList<Patient>>{
        val patientDataSource : DataSource.Factory<Int, Patient> = patientRepository.findPatientByMultipleFilters(firstName, middleName, lastName, gender)
        return patientDataSource.toLiveData(20)
    }

    suspend fun findLastPatient() : String {
        return patientRepository.findLastPatient()
    }

    private suspend fun performInsert(patient: Patient) = withContext(Dispatchers.IO){
        patientRepository.insert(patient)
    }
}