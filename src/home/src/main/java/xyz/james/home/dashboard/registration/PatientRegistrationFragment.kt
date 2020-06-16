/*
 * *
 *  * Created by Jeremy James on 6/16/20 10:35 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 6/10/20 9:05 AM
 *
 */

package xyz.james.home.dashboard.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import xyz.james.home.databinding.FragmentPatientRegistrationBinding
import xyz.james.home.viewmodels.PatientViewModel


/**
 * A simple [Fragment] subclass.
 */
class PatientRegistrationFragment : Fragment() {

    val viewModel: PatientViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentPatientRegistrationBinding = FragmentPatientRegistrationBinding.inflate(inflater, container, false)

        binding.vm = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getInserted().observe(viewLifecycleOwner, Observer<Boolean> {
            if (it)
                Snackbar.make(view, "Patient Registered Successfully!", Snackbar.LENGTH_LONG).show()
            else
                Snackbar.make(view, "Failed to register patient!", Snackbar.LENGTH_LONG).show()
        })
    }
}
