package com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.kerempurcek.appointmenttrackingsystem.R
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.adapter.OwnerAppointmentAdapter
import com.kerempurcek.appointmenttrackingsystem.databinding.FragmentBarberMainBinding
import com.kerempurcek.appointmenttrackingsystem.databinding.FragmentOwnerAppointmentBinding

class OwnerAppointmentFragment : Fragment() {
    private var _binding: FragmentOwnerAppointmentBinding? = null

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
        _binding = FragmentOwnerAppointmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeButton.setOnClickListener {
            ownerAppointmenttoHome(it)
        }
        binding.userButton.setOnClickListener {
            ownerAppointmenttoUser(it)
        }


        val appointmentList = listOf(
            listOf("Mehmet Yılmaz", "14/02/2025", "14:30"),
            listOf("Ahmet Kaya", "15/02/2025", "10:00"),
            listOf("Zeynep Demir", "18/02/2025", "16:45"),
            listOf("Elif Çelik", "20/02/2025", "13:15"),
            listOf("Murat Öz", "22/02/2025", "09:00")
        )

        val adapter = OwnerAppointmentAdapter(appointmentList)
        binding.RecyclerViewAppointment.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL, false)
        binding.RecyclerViewAppointment.adapter=adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun ownerAppointmenttoHome(view: View) {
        val action = OwnerAppointmentFragmentDirections.actionOwnerAppointmentFragmentToBarberMainFragment()
        Navigation.findNavController(view).navigate(action)
    }
    fun ownerAppointmenttoUser(view: View){
        val action = OwnerAppointmentFragmentDirections.actionOwnerAppointmentFragmentToOwnerUserFragment()
        Navigation.findNavController(view).navigate(action)
    }

}