package com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.kerempurcek.appointmenttrackingsystem.R
import com.kerempurcek.appointmenttrackingsystem.databinding.FragmentAppoinmentBinding
import com.kerempurcek.appointmenttrackingsystem.databinding.FragmentApproveAppointmentBinding
import com.kerempurcek.appointmenttrackingsystem.databinding.FragmentUserPageBinding


class ApproveAppointment : Fragment() {
    private var _binding: FragmentApproveAppointmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentApproveAppointmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       binding.OkButton.setOnClickListener {
           approvetoHomePage(it)
       }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun approvetoHomePage(view: View){

        val action = ApproveAppointmentDirections.actionApproveAppointmentToUserMainFragment()
        Navigation.findNavController(view).navigate(action)
    }

}