/*
 * *
 *  * Created by Jeremy James on 6/16/20 10:35 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 6/2/20 6:24 PM
 *
 */

package xyz.james.db.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import java.lang.reflect.Type
import java.util.*


class Converters {

    @TypeConverter
    fun fromString(value: String) : ArrayList<String>{
        val listType: Type = object : TypeToken<ArrayList<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<String>) : String {
        val gson = Gson()
        val json = gson.toJson(list)
        return json
    }

    @TypeConverter
    fun fromLocalDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it, DateTimeFormatter.ofPattern("dd/MM/yyyy")) }
    }

    @TypeConverter
    fun localDateToString(date: LocalDate?): String? {
        return date?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }

    @TypeConverter
    fun fromLocalTime(value: String?) : LocalTime? {
        return value?.let { LocalTime.parse(it, DateTimeFormatter.ofPattern("hh:mm a")) }
    }

    @TypeConverter
    fun localTimeToString(time: LocalTime?): String? {
        return time?.format(DateTimeFormatter.ofPattern("hh:mm a"))
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}