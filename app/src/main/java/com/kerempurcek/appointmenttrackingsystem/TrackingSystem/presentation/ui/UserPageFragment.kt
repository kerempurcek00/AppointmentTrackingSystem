package com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.kerempurcek.appointmenttrackingsystem.R
import com.kerempurcek.appointmenttrackingsystem.databinding.FragmentUserPageBinding


class UserPageFragment : Fragment() {

    private var _binding: FragmentUserPageBinding? = null

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
        _binding = FragmentUserPageBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeButton.setOnClickListener {
            usertohome(it)
        }
        binding.appoinmentButton.setOnClickListener {
            usertoappoinment(it)
        }

        // Randevu iptal buton rengi değiştirme
        binding.cancelButton.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.backgroundtint)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun usertohome(view: View) {
        val action = UserPageFragmentDirections.actionUserPageFragmentToUserMainFragment()
        Navigation.findNavController(view).navigate(action)
    }

    fun usertoappoinment(view: View){
        val action = UserPageFragmentDirections.actionUserPageFragmentToAppoinmentFragment()
        Navigation.findNavController(view).navigate(action)
    }
}