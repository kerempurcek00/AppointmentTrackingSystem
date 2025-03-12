package com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import com.google.android.material.navigation.NavigationBarPresenter
import com.kerempurcek.appointmenttrackingsystem.R
import com.kerempurcek.appointmenttrackingsystem.databinding.FragmentLoginPageBinding
import java.io.Console
import java.util.Locale


class LoginPage : Fragment() {
    private var _binding: FragmentLoginPageBinding? = null

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
        _binding = FragmentLoginPageBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ToggleGroup'dan buton seçimi yapılınca
        // Renkleri tanımla
        val selectedColor = ColorStateList.valueOf(Color.parseColor("#18A0FB"))
        val defaultColor = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
        val selectedTextColor = Color.parseColor("#FFFFFF")
        val defaultTextColor = Color.parseColor("#000000")

        binding.ToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.UserButton -> {
                        binding.UserButton.backgroundTintList = selectedColor
                        binding.OwnerButton.backgroundTintList = defaultColor
                        binding.UserButton.setTextColor(selectedTextColor)
                        binding.OwnerButton.setTextColor(defaultTextColor)
                    }

                    R.id.OwnerButton -> {
                        binding.OwnerButton.backgroundTintList = selectedColor
                        binding.UserButton.backgroundTintList = defaultColor
                        binding.OwnerButton.setTextColor(selectedTextColor)
                        binding.UserButton.setTextColor(defaultTextColor)
                    }
                }
            }
        }
        binding.registerLogin.setOnClickListener {
            register(it)
        }

        //Parola Alt Çizgi
        binding.TextForgotPassword.paintFlags = binding.TextForgotPassword.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        // Kullanıcı seçimi tutmak için değişkenler
        var isUserButtonSelected = false
        var isOwnerButtonSelected = false
        // UserButton'a tıklanma durumu
        binding.UserButton.setOnClickListener {
            isUserButtonSelected = true
            isOwnerButtonSelected = false // Diğer butonu sıfırla
        }
        // OwnerButton'a tıklanma durumu
        binding.OwnerButton.setOnClickListener {
            isUserButtonSelected = false // Diğer butonu sıfırla
            isOwnerButtonSelected = true
        }

        //giriş yaptıktan sonra anasayfaya gelmek
        binding.LoginButton.setOnClickListener {

            if(isUserButtonSelected){
                LoginToHome(it)

            }else if(isOwnerButtonSelected){
                LogintoOwnerHome(it)
            }else{
                Toast.makeText(requireContext(),"Lütfen Kullanıcı Seçimi Yapınız! ",Toast.LENGTH_LONG).show()
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        // ViewBinding nesnesini null yaparak bellek sızıntılarını engelle
        _binding = null
    }


    fun register(view: View) {

        val action = LoginPageDirections.actionLoginPageToRegisterFragment()
        findNavController(view).navigate(action)
    }

    fun LoginToHome(view: View){
        val action = LoginPageDirections.actionLoginPageToUserMainFragment()
        findNavController(view).navigate(action)


    }

    fun LogintoOwnerHome(view: View){
        val action = LoginPageDirections.actionLoginPageToBarberMainFragment()
        Navigation.findNavController(view).navigate(action)
    }


}


