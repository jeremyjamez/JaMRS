/*
 * *
 *  * Created by Jeremy James on 6/16/20 10:35 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 6/10/20 9:05 AM
 *
 */

package xyz.james.db.entities

import androidx.annotation.NonNull
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.Year

@Entity(tableName = "patient")
data class Patient(
    val firstName : String = "",
    val middleName : String = "",
    val lastName: String = "",
    @Embedded val address: Address,
    val dob: String = "",
    val gender: String = "",
    val medicalConditions: ArrayList<String> = ArrayList(),
    val familyMedicalHistory: ArrayList<String> = ArrayList()
){
    @PrimaryKey @NonNull private var patientId: String = ""

    fun getPatientId(): String {
        return patientId
    }

    fun setPatientId(patientId: String){
        this.patientId = patientId
    }

    fun generatePatientId(lastId: String?){
        val year = Year.now()
        if(lastId.isNullOrBlank())
            patientId = "$year" + "0000"
        else {
            if (lastId.startsWith("$year"))
                patientId = "${lastId.toInt() + 1}"
        }
    }

    fun patientFullNameWithMiddleInitial(): String {
        return "$firstName ${middleName[0]}. $lastName"
    }

    override fun toString(): String {
        return "Patient(firstName='$firstName', middleName='$middleName', lastName='$lastName', address=$address, dob='$dob', gender='$gender', medicalConditions=$medicalConditions, familyMedicalHistory=$familyMedicalHistory, patientId=$patientId)"
    }


}