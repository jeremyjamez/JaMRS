/*
 * *
 *  * Created by Jeremy James on 6/16/20 10:35 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 6/13/20 9:58 PM
 *
 */

package xyz.james.db.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.threeten.bp.LocalDate
import xyz.james.db.entities.Appointment

@Dao
interface AppointmentDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(appointment: Appointment) : Long

    @Query("select * from appointment where appointmentDate like :date")
    fun findAppointmentsByDate(date: LocalDate) : DataSource.Factory<Int, Appointment>

    @Query("select * from appointment")
    fun findAllAppointments() : DataSource.Factory<Int, Appointment>
}