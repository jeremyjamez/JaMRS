/*
 * *
 *  * Created by Jeremy James on 6/16/20 10:35 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 6/13/20 10:25 PM
 *
 */

package xyz.james.db.entities

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import org.threeten.bp.Year
import org.threeten.bp.format.DateTimeFormatter

@Entity(tableName = "appointment",
    foreignKeys = arrayOf(
        ForeignKey(entity = Patient::class,
            parentColumns = arrayOf("patientId"),
            childColumns = arrayOf("patientId"),
            onDelete = ForeignKey.RESTRICT
        )
    )
)
data class Appointment (
    val patientId: String,
    val appointmentDate: String,
    val appointmentTime: String,
    val appointmentNote: String
) {
    @PrimaryKey @NonNull
    private var appointmentId: String = ""

    fun getAppointmentId(): String {
        return appointmentId
    }

    fun setAppointmentId(id: String){
        this.appointmentId = id
    }

    @Ignore
    private val charPool : List<Char> = ('A'..'Z') + ('0'..'9')
    @Ignore
    private val STRING_LENGTH = 5;

    private fun generateAppointmentId(){
        val year = Year.now()
        val randomString = (1..STRING_LENGTH)
            .map { i -> kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("");
        appointmentId = "${year.format(DateTimeFormatter.ofPattern("ty"))}$randomString"
    }
}