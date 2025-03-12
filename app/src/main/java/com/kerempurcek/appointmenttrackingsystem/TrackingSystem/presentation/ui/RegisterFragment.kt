package com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.kerempurcek.appointmenttrackingsystem.R
import com.kerempurcek.appointmenttrackingsystem.databinding.FragmentLoginPageBinding
import com.kerempurcek.appointmenttrackingsystem.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
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
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Kayıt Ol Butonuna Basıldığında Giriş Sayfasına Yönlendirme
        binding.RegisterPageButton.setOnClickListener{RegisterToLogin(it)}

    }

    override fun onDestroyView() {
        super.onDestroyView()
        // ViewBinding nesnesini null yaparak bellek sızıntılarını engelle
        _binding = null
    }

fun RegisterToLogin(view: View){
    val action = RegisterFragmentDirections.actionRegisterFragmentToLoginPage()
    Navigation.findNavController(view).navigate(action)
}


}