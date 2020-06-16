/*
 * *
 *  * Created by Jeremy James on 6/16/20 10:35 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 6/13/20 10:26 PM
 *
 */

package xyz.james.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import xyz.james.db.dao.AppointmentDao
import xyz.james.db.dao.PatientDao
import xyz.james.db.entities.Appointment
import xyz.james.db.entities.Chat
import xyz.james.db.entities.MedicalRecord
import xyz.james.db.entities.Patient
import xyz.james.db.utils.Converters

@Database(entities = arrayOf(Patient::class, Chat::class, MedicalRecord::class, Appointment::class), version = 4, exportSchema = true)
@TypeConverters(Converters::class)
abstract class OfflineDatabase : RoomDatabase() {
    abstract fun patientDao() : PatientDao
    abstract fun appointmentDao() : AppointmentDao
}