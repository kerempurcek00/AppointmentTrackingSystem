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
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationBarPresenter
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.kerempurcek.appointmenttrackingsystem.R
import com.kerempurcek.appointmenttrackingsystem.databinding.FragmentLoginPageBinding
import java.io.Console
import java.util.Locale


class LoginPage : Fragment() {
    private var _binding: FragmentLoginPageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        db = Firebase.firestore

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


        binding.registerLogin.setOnClickListener {
            register(it)
        }



        val currentUser = auth.currentUser
        if (currentUser != null) {

            binding.root.visibility = View.INVISIBLE

            //eğer kullanıcı giriş yaptıysa o çıkış yapana kadar login ekranına götürmemize gerek yok
            val UserId = currentUser.uid
            db.collection("UserTypes").document(UserId).get().addOnSuccessListener { document ->
                val role = document.get("userType")
                if (role == "Berber") {

                    val action = LoginPageDirections.actionLoginPageToBarberMainFragment()
                    findNavController().navigate(action)

                } else {
                    val action = LoginPageDirections.actionLoginPageToUserMainFragment()
                    findNavController().navigate(action)
                }

            }

        }


        //giriş yaptıktan sonra anasayfaya gelmek
        binding.LoginButton.setOnClickListener {

            LoginToHome(it)

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

    fun LoginToHome(view: View) {
        val email = binding.LoginEmail.text.toString()
        val password = binding.LoginPassword.text.toString()



        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    val UserId = currentUser.uid
                    db.collection("UserTypes").document(UserId).get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                val role = document.get("userType")
                                if (role == "Berber") {
                                    val action =
                                        LoginPageDirections.actionLoginPageToBarberMainFragment()
                                    findNavController().navigate(action)

                                } else {
                                    val action =
                                        LoginPageDirections.actionLoginPageToUserMainFragment()
                                    findNavController().navigate(action)

                                }

                            }


                        }
                }

            }.addOnFailureListener { exception ->
                // Yanlış Email veya Parola
                if (exception is FirebaseAuthInvalidCredentialsException || exception is FirebaseAuthInvalidUserException) {

                    Toast.makeText(requireContext(), "Hatalı email veya parola!", Toast.LENGTH_LONG)
                        .show()
                } else {
                    // Diğer Hatalar
                    Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG)
                        .show()

                }
            }
        }else {
            Toast.makeText(requireContext(), "Email ve parola boş olamaz!", Toast.LENGTH_SHORT).show()
        }




    }


}


