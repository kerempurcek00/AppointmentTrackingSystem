package com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.kerempurcek.appointmenttrackingsystem.R
import com.kerempurcek.appointmenttrackingsystem.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    // UserType
    var selectedItem = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        db = Firebase.firestore

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
        binding.RegisterPageButton.setOnClickListener { RegisterToLogin(it) }

        getspinnerdata()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        // ViewBinding nesnesini null yaparak bellek sızıntılarını engelle
        _binding = null
    }

    fun RegisterToLogin(view: View) {

        //Firebase Auth
        val email = binding.TextEmailRegister.text.toString()
        val password = binding.TextPasswordRegister.text.toString()


        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { createUser ->
                if (createUser.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (!userId.isNullOrEmpty()) {
                        addDataUserType()
                    }

                    val action = RegisterFragmentDirections.actionRegisterFragmentToLoginPage()
                    Navigation.findNavController(view).navigate(action)

                }

            }.addOnFailureListener { exception ->
                Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG)
                    .show()

            }

        }


    }

    fun getspinnerdata() {
        val spinner = binding.spinnerUserBarber

        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item)
        adapter.add(getString(R.string.user))
        adapter.add(getString(R.string.barber))

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.UserTypesSelection),
                    Toast.LENGTH_LONG
                ).show()
                selectedItem = parent?.getItemAtPosition(position).toString()
                println(selectedItem)

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Hiçbir öğe seçilmediğinde yapılacak işlemler

            }
        }
    }

    fun addDataUserType() {

        // FireStore
        val UserTypeMap = hashMapOf<String, Any>()
        val UserId = auth.currentUser?.uid    // userID neyse döküman id olacak
        UserTypeMap.put("userType", selectedItem)
        UserTypeMap.put("nameSurname", binding.TextViewNameSurname.text.toString())

        db.collection("UserTypes").document(UserId!!).set(UserTypeMap)
    }

}