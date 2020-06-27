/*
 * *
 *  * Created by Jeremy James on 6/16/20 10:35 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 6/13/20 10:10 PM
 *
 */

package xyz.james.db.repositories.impl

import androidx.paging.DataSource
import org.threeten.bp.LocalDate
import xyz.james.db.dao.AppointmentDao
import xyz.james.db.entities.Appointment
import xyz.james.db.repositories.AppointmentRepository

class AppointmentRepositoryImpl(val appointmentDao: AppointmentDao) : AppointmentRepository {

    override suspend fun insert(appointment: Appointment): Long {
        return appointmentDao.insert(appointment)
    }

    override fun findAllAppointments(): DataSource.Factory<Int, Appointment> {
        return appointmentDao.findAllAppointments()
    }

    override fun findAppointmentsByDate(date: LocalDate): DataSource.Factory<Int, Appointment> {
        return appointmentDao.findAppointmentsByDate(date)
    }
}