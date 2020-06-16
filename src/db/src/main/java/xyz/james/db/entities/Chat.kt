/*
 * *
 *  * Created by Jeremy James on 6/16/20 10:35 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 6/6/20 11:40 AM
 *
 */

package xyz.james.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import java.util.*

@Fts4
@Entity(tableName = "chat")
data class Chat(
    @PrimaryKey @ColumnInfo(name = "rowid") val chatId: Int,
    val message: ArrayList<String>,
    val image: ArrayList<String>,
    val document: ArrayList<String>,
    val timestamp: Date
)