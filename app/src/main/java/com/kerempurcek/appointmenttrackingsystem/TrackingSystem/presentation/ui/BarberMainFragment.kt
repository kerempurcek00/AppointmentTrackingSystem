package com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.kerempurcek.appointmenttrackingsystem.R
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.adapter.HomeAdapter
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.viewmodal.FireStoreViewModel
import com.kerempurcek.appointmenttrackingsystem.databinding.FragmentBarberMainBinding


class BarberMainFragment : Fragment() {
    private var _binding: FragmentBarberMainBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var db:FirebaseFirestore
    private lateinit var viewModel: FireStoreViewModel
    private var adapter: HomeAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Firebase.firestore

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBarberMainBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.homeButton.setOnClickListener {
            updateIconHome(binding.homeButton)
        }
        binding.userButton.setOnClickListener {
            updateIconUser(binding.userButton)
            ownerUserPage(it)

        }
        binding.appoinmentButton.setOnClickListener {
            updateIconAppoinment(binding.appoinmentButton)
            barberMaintoAppoinment(it)

        }
        // ViewModel'i başlatıyoruz
        viewModel = ViewModelProvider(this).get(FireStoreViewModel::class.java)
        // Adapter & FireStore Data
        adapter = HomeAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter =adapter

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.ProgressBarOwMain.visibility = View.VISIBLE
            } else {
                binding.ProgressBarOwMain.visibility = View.GONE
            }
        }
        // Veriyi gözlemle
        viewModel.owners.observe(viewLifecycleOwner) { list ->
            adapter?.updateList(list) // Adapter'a yeni listeyi ver
        }
        // Firestore'dan veri çek
        viewModel.getFireStoreData()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun updateIconHome(clickedButton: ImageButton) {
        // Bütün butonlar başlangıç ikonuna sıfırlandı
        binding.homeButton.setImageResource(R.drawable.home2)
        binding.userButton.setImageResource(R.drawable.user2)
        binding.appoinmentButton.setImageResource(R.drawable.appoinment1)

        //tıklanan butonun ikonu değişicek
        clickedButton.setImageResource(R.drawable.home)
    }

    fun updateIconUser(clickedButton: ImageButton) {
        binding.homeButton.setImageResource(R.drawable.home2)
        binding.userButton.setImageResource(R.drawable.user2)
        binding.appoinmentButton.setImageResource(R.drawable.appoinment1)

        clickedButton.setImageResource(R.drawable.usericon)

    }

    fun updateIconAppoinment(clickedButton: ImageButton) {
        binding.homeButton.setImageResource(R.drawable.home2)
        binding.userButton.setImageResource(R.drawable.user2)
        binding.appoinmentButton.setImageResource(R.drawable.appoinment1)

        clickedButton.setImageResource(R.drawable.appoinment2)



    }

    fun ownerUserPage(view: View) {
        val action = BarberMainFragmentDirections.actionBarberMainFragmentToOwnerUserFragment()
        Navigation.findNavController(view).navigate(action)

    }
    fun barberMaintoAppoinment(view: View){
        val action = BarberMainFragmentDirections.actionBarberMainFragmentToOwnerAppointmentFragment()
        Navigation.findNavController(view).navigate(action)

    }


}