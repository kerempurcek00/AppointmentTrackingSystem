package com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.kerempurcek.appointmenttrackingsystem.R
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.domain.model.AppointmentList
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.domain.model.DeleteAppointmentList
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.domain.model.GetShopNameList
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.viewmodal.FireStoreViewModel
import com.kerempurcek.appointmenttrackingsystem.databinding.FragmentUserPageBinding


class UserPageFragment : Fragment() {

    private var _binding: FragmentUserPageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    val DeleteAppointmentList: ArrayList<DeleteAppointmentList> = arrayListOf()
    private lateinit var  viewModel: FireStoreViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        db = Firebase.firestore

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
        viewModel = ViewModelProvider(this)[FireStoreViewModel::class.java]



        binding.homeButton.setOnClickListener {
            usertohome(it)
        }
        binding.appoinmentButton.setOnClickListener {
            usertoappoinment(it)
        }


        // Randevu iptal buton rengi değiştirme
        binding.cancelButton.backgroundTintList =
            ContextCompat.getColorStateList(requireContext(), R.color.backgroundtint)
        binding.logout.setOnClickListener {
            auth.signOut()
            val action = UserPageFragmentDirections.actionUserPageFragmentToLoginPage()
            Navigation.findNavController(view).navigate(action)
        }
        nameSurname()

        getAppointmentData()


        binding.cancelButton.setOnClickListener {
            CancelAppointment()
        }




    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun usertohome(view: View) {
        val action = UserPageFragmentDirections.actionUserPageFragmentToUserMainFragment()
        Navigation.findNavController(view).navigate(action)
    }

    fun usertoappoinment(view: View) {
        val action = UserPageFragmentDirections.actionUserPageFragmentToAppoinmentFragment()
        Navigation.findNavController(view).navigate(action)
    }

    private fun getAppointmentData() {
        binding.CardView.visibility=View.GONE
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                // Yükleme başladığında ProgressBar'ı göster
                binding.progressBar.visibility = View.VISIBLE
            } else {
                // Yükleme bittiğinde ProgressBar'ı gizle
                binding.progressBar.visibility = View.GONE
            }
        }

        viewModel.getAppointmentData()
        viewModel.AppointmentLists.observe(viewLifecycleOwner){appointments->
            val currentUser = auth.currentUser
            if (appointments != null && currentUser?.email == appointments.email) {
                activity?.runOnUiThread {
                    binding.BarberShopName.setText(appointments.shopName)
                    binding.AppointmentDateTime.setText(appointments.date)
                }
                binding.CardView.visibility = View.VISIBLE

            }

        }

    }

    private fun CancelAppointment(){
        val currentUser = auth.currentUser
        db.collection("CustomerAppointments").get().addOnSuccessListener {result->

                for (document in result) {
                    val email = document.get("email") as String
                    val documentId = document.id
                    if (currentUser != null && currentUser.email == email && documentId != null) {
                        val list = DeleteAppointmentList(email)
                        DeleteAppointmentList.add(list)
                        db.collection("CustomerAppointments").document(documentId).delete()
                        Toast.makeText(
                            requireContext(),
                            "Randevunuz silinmiştir!",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }

            binding.CardView.visibility = View.GONE


        }

    }





    private fun nameSurname(){

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                // Yükleme başladığında ProgressBar'ı göster
                binding.progressBar.visibility = View.VISIBLE
            } else {
                // Yükleme bittiğinde ProgressBar'ı gizle
                binding.progressBar.visibility = View.GONE
            }
        }

        viewModel.nameSurname()
        viewModel.nameSurnameList.observe(viewLifecycleOwner){userData->
            if(userData!=null){
                binding.TextNameSurname.setText(userData.nameSurname)
            }

        }

    }



}


