/*
 * *
 *  * Created by Jeremy James on 6/16/20 10:35 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 6/13/20 10:10 PM
 *
 */

package xyz.james.db.repositories

import xyz.james.db.entities.Appointment

interface AppointmentRepository {
    suspend fun insert(appointment: Appointment) : Long
}