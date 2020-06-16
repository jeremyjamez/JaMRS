/*
 * *
 *  * Created by Jeremy James on 6/16/20 10:35 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 6/10/20 9:09 AM
 *
 */

package xyz.james.home.dashboard.search

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_search_patient.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import xyz.james.home.R
import xyz.james.home.viewmodels.PatientViewModel

/**
 * A simple [Fragment] subclass.
 */
class SearchPatientFragment : Fragment() {

    val patientViewModel: PatientViewModel by viewModel()
    lateinit var searchView: SearchView
    private val adapter = SearchAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_patient, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        /*filter_toggle.addOnButtonCheckedListener { _, _, isChecked ->
            if(isChecked)
                filter_chips_container.visibility = View.VISIBLE
            else
                filter_chips_container.visibility = View.GONE
        }

        filter_chip_group.setOnCheckedChangeListener(this)*/
        search_recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
        val searchItem = menu.findItem(R.id.action_search)
        searchItem.expandActionView()
        searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Enter Patient ID"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(!newText.isNullOrBlank()){
                    patientViewModel.findPatientById(newText).observe(viewLifecycleOwner, Observer {
                        adapter.submitList(it)
                    })
                }
                return false
            }

        })
    }

    /*override fun onCheckedChanged(group: ChipGroup?, checkedId: Int) {
        when(checkedId){
            R.id.chip_firstname_filter -> {
                if(!searchView.query.isNullOrBlank()){
                    searchView.setQuery("First Name: ", false)
                }
            }
        }
    }*/
}
