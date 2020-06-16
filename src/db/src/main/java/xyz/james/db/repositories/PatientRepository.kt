/*
 * *
 *  * Created by Jeremy James on 6/16/20 10:35 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 6/12/20 8:10 PM
 *
 */

package xyz.james.db.repositories

import androidx.paging.DataSource
import xyz.james.db.entities.Patient

interface PatientRepository {
    fun getAllPatients() : DataSource.Factory<Int, Patient>
    fun findPatientById(id: String) : DataSource.Factory<Int, Patient>
    fun findPatientByMultipleFilters(firstName: String, middleName: String, lastName: String, gender: String) : DataSource.Factory<Int, Patient>
    suspend fun findLastPatient() : String
    suspend fun insert(patient: Patient): Long
}