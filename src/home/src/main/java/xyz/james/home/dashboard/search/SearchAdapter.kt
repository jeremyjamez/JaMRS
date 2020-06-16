/*
 * *
 *  * Created by Jeremy James on 6/16/20 10:35 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 6/9/20 4:48 PM
 *
 */

package xyz.james.home.dashboard.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import xyz.james.db.entities.Patient
import xyz.james.home.R

class SearchAdapter() :
    PagedListAdapter<Patient, SearchViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false))
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val patient: Patient? = getItem(position)
        holder.searchPatientName.text = patient?.patientFullNameWithMiddleInitial()
        holder.searchPatientId.text = "${patient?.getPatientId()}"
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Patient>() {
            // The ID property identifies when items are the same.
            override fun areItemsTheSame(oldItem: Patient, newItem: Patient) =
                oldItem.getPatientId() == newItem.getPatientId()

            // If you use the "==" operator, make sure that the object implements
            // .equals(). Alternatively, write custom data comparison logic here.
            override fun areContentsTheSame(
                oldItem: Patient, newItem: Patient) = oldItem == newItem
        }
    }
}