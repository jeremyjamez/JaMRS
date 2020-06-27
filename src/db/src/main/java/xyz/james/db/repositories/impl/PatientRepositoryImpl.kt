/*
 * *
 *  * Created by Jeremy James on 6/16/20 10:35 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 6/12/20 8:10 PM
 *
 */

package xyz.james.db.repositories.impl

import androidx.paging.DataSource
import xyz.james.db.dao.PatientDao
import xyz.james.db.entities.Patient
import xyz.james.db.repositories.PatientRepository

class PatientRepositoryImpl(val patientDao: PatientDao) : PatientRepository {

    override suspend fun insert(patient: Patient) : Long {
        return patientDao.insert(patient)
    }

    override fun findPatientsById(id: String): DataSource.Factory<Int, Patient> {
        return patientDao.findPatientsById(id)
    }

    override suspend fun findPatientById(id: String): Patient {
        return patientDao.findPatientById(id)
    }

    override fun getAllPatients(): DataSource.Factory<Int, Patient> {
        return patientDao.getAllPatients()
    }

    override suspend fun findLastPatient(): String {
        return patientDao.findLastPatient()
    }
}