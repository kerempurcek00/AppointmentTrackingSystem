package com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.kerempurcek.appointmenttrackingsystem.R
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.adapter.HomeAdapter
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.adapter.OwnerAppointmentAdapter
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.domain.model.AppointmentList
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.domain.model.EditOwnerDataClass
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.viewmodal.FireStoreViewModel
import com.kerempurcek.appointmenttrackingsystem.databinding.FragmentBarberMainBinding
import com.kerempurcek.appointmenttrackingsystem.databinding.FragmentOwnerAppointmentBinding

class OwnerAppointmentFragment : Fragment() {
    private var _binding: FragmentOwnerAppointmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var db:FirebaseFirestore
    private lateinit var  auth:FirebaseAuth
    val CustomerAppointments:ArrayList<AppointmentList> = arrayListOf()
    private  var adapter: OwnerAppointmentAdapter? = null
    private lateinit var viewModel: FireStoreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Firebase.firestore
        auth = Firebase.auth

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
        viewModel = ViewModelProvider(this)[FireStoreViewModel::class.java]

        binding.homeButton.setOnClickListener {
            ownerAppointmenttoHome(it)
        }
        binding.userButton.setOnClickListener {
            ownerAppointmenttoUser(it)
        }


        // RecyclerView için adapter'ı burada tanımlıyoruz
        adapter = OwnerAppointmentAdapter(CustomerAppointments)
        binding.RecyclerViewAppointment.layoutManager = LinearLayoutManager(requireContext())
        binding.RecyclerViewAppointment.adapter = adapter
        binding.ProgressBarOwAppointment.visibility = View.GONE
        // Veriyi çekme işlemi başlatılıyor
        getCustomerAppointment()







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



    private fun getCustomerAppointment() {
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.ProgressBarOwAppointment.visibility = View.VISIBLE
            } else {
                binding.ProgressBarOwAppointment.visibility = View.GONE
            }
        }
        viewModel.CustomerAppointments.observe(viewLifecycleOwner) { list ->
            adapter?.updateList(list) // Adapter'a yeni listeyi ver
        }
        // Firestore'dan veri çek
        viewModel.getCustomerAppointment()

    }



}