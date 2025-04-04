package com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.kerempurcek.appointmenttrackingsystem.databinding.FragmentAppoinmentBinding
import com.kerempurcek.appointmenttrackingsystem.databinding.FragmentBottomSheetBinding

class BottomSheetFragment : Fragment() {

    private var _binding: FragmentBottomSheetBinding? = null

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
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.MainConstraintLayout.setOnClickListener {
            SheettoAppointmentFragment(it)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun SheettoAppointmentFragment(view: View){
        val action = BottomSheetFragmentDirections.actionBottomSheetFragmentToAppoinmentFragment()
        Navigation.findNavController(view).navigate(action)

    }



}
