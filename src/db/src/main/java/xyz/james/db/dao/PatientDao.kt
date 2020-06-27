/*
 * *
 *  * Created by Jeremy James on 6/16/20 10:35 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 6/12/20 8:10 PM
 *
 */

package xyz.james.db.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import xyz.james.db.entities.Patient

@Dao
interface PatientDao {

    @Query("select * from patient where patientId LIKE '%' || :id ||'%'")
    fun findPatientsById(id: String) : DataSource.Factory<Int, Patient>

    @Query("select * from patient where patientId like :id")
    suspend fun findPatientById(id: String) : Patient

    @Query("select * from patient where firstName like :firstName or middleName like :middleName or lastName like :lastName or gender like :gender")
    fun findPatientByMultipleFilters(firstName: String, middleName: String, lastName: String, gender: String) : DataSource.Factory<Int, Patient>

    @Query("select * from patient order by patientId asc")
    fun getAllPatients() : DataSource.Factory<Int, Patient>

    @Query("select patientId from patient where patientId=(select max(patientId) from patient)")
    suspend fun findLastPatient() : String

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(patient: Patient) : Long
}