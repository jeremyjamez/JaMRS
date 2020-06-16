/*
 * *
 *  * Created by Jeremy James on 6/16/20 10:35 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 6/5/20 9:15 AM
 *
 */

package xyz.james.home.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import kotlinx.android.synthetic.main.fragment_dashboard.*
import xyz.james.home.R
import xyz.james.home.models.DashboardItem

/**
 * A simple [Fragment] subclass.
 */
class DashboardFragment : Fragment() {

    var colours: IntArray? = null
    val colourList : ArrayList<Int> = ArrayList<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val dashboardItems = arrayListOf<DashboardItem>(
            DashboardItem("Register Patient", requireContext().getString(R.string.register_patient_caption), R.drawable.ic_user),
            DashboardItem("Search Patient", requireContext().getString(R.string.search_patient_caption), R.drawable.ic_search),
            DashboardItem("Appointments", requireContext().getString(R.string.appoint_patient_caption), R.drawable.ic_calendar),
            DashboardItem("Record Info", requireContext().getString(R.string.record_patient_caption), R.drawable.ic_activity),
            DashboardItem("Consultation", requireContext().getString(R.string.consult_patient_caption), R.drawable.ic_message_square)
        )

        colours = requireContext().resources.getIntArray(R.array.dashboard_colours)

        colours!!.forEach {
            colourList.add(it)
        }

        dashboard_recyclerView.adapter = DashboardAdapter(dashboardItems, requireView(), colourList)
    }

    class DashboardAdapter(val dashboardItems: ArrayList<DashboardItem>, val view: View, val colours: ArrayList<Int>) : RecyclerView.Adapter<DashboardViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
            return DashboardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.dashboard_item, parent, false))
        }

        override fun getItemCount(): Int {
            return dashboardItems.size
        }

        override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
            holder.title.text = dashboardItems[position].name
            holder.caption.text = dashboardItems[position].caption
            holder.img.setImageResource(dashboardItems[position].icon)
            holder.card.setCardBackgroundColor(colours[position])

            holder.itemView.setOnClickListener {
                when(dashboardItems[position].name){
                    "Register Patient" -> {
                        val action = DashboardFragmentDirections.actionDashboardFragmentToPatientRegistrationFragment()
                        view.findNavController().navigate(action)
                    }
                    "Search Patient" -> {
                        val action = DashboardFragmentDirections.actionDashboardFragmentToSearchPatientFragment()
                        view.findNavController().navigate(action)
                    }
                    "Appointments" -> {
                        val action = DashboardFragmentDirections.actionDashboardFragmentToAppointmentFragment()
                        view.findNavController().navigate(action)
                    }
                    "Consultation" -> {
                        val action = DashboardFragmentDirections.actionDashboardFragmentToConsultationFragment()
                        view.findNavController().navigate(action)
                    }
                    "Record Info" -> {
                        val action = DashboardFragmentDirections.actionDashboardFragmentToMedicalRecordFragment()
                        view.findNavController().navigate(action)
                    }
                }
            }
        }

    }

    class DashboardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title = itemView.findViewById<MaterialTextView>(R.id.dashboard_item_title)
        val caption = itemView.findViewById<MaterialTextView>(R.id.dashboard_item_caption)
        val img = itemView.findViewById<AppCompatImageView>(R.id.dashboard_item_icon)
        val card = itemView.findViewById<MaterialCardView>(R.id.dashboard_card)
    }
}
