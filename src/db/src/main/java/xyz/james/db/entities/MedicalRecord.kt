/*
 * *
 *  * Created by Jeremy James on 6/16/20 10:35 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 6/13/20 7:38 PM
 *
 */

package xyz.james.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "medicalRecord",
    foreignKeys = arrayOf(
        ForeignKey(entity = Patient::class,
            parentColumns = arrayOf("patientId"),
            childColumns = arrayOf("pid"),
            onDelete = ForeignKey.RESTRICT
        )
    )
)
data class MedicalRecord (
    @PrimaryKey(autoGenerate = true) val medicalId: Int,
    val pid: String,
    val dateRecorded: Date? = null,
    val bpm: Int? = null,
    val weight: Double? = null,
    val height: Double? = null,
    val pressure: String? = null,
    val doctorNotes: String? = null
)