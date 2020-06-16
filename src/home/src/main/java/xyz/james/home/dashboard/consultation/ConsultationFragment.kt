/*
 * *
 *  * Created by Jeremy James on 6/16/20 10:35 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 6/4/20 11:44 AM
 *
 */

package xyz.james.home.dashboard.consultation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_consultation.*
import xyz.james.home.R

/**
 * A simple [Fragment] subclass.
 */
class ConsultationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_consultation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab_new_chat.setOnClickListener {
            val action = ConsultationFragmentDirections.actionConsultationFragmentToChatFragment()
            findNavController().navigate(action)
        }
    }
}