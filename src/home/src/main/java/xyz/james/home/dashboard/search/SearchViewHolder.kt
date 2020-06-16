/*
 * *
 *  * Created by Jeremy James on 6/16/20 10:35 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 6/8/20 7:14 PM
 *
 */

package xyz.james.home.dashboard.search

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import xyz.james.home.R

class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val searchPatientName = itemView.findViewById<MaterialTextView>(R.id.searchPatientName)
    val searchPatientId = itemView.findViewById<MaterialTextView>(R.id.searchPatientId)
}