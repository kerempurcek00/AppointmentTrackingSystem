package com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.kerempurcek.appointmenttrackingsystem.R
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.adapter.HomeAdapter
import com.kerempurcek.appointmenttrackingsystem.databinding.FragmentRegisterBinding
import com.kerempurcek.appointmenttrackingsystem.databinding.FragmentUserMainBinding


class UserMainFragment : Fragment() {
    private var _binding: FragmentUserMainBinding? = null
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
        _binding = FragmentUserMainBinding.inflate(inflater, container, false)
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
            hometouser(it)
        }
        binding.appoinmentButton.setOnClickListener {
            updateIconAppoinment(binding.appoinmentButton)
            //randevu sayfasına geçme
            appoinmentPage(it)
        }


        val dummyList = listOf(
            mapOf(
                "ShopName" to "Berberim",
                "Owner" to "Ahmet Yılmaz",
                "PhoneNumber" to "0555 123 45 67",
                "Price" to "150 TL"
            ),
            mapOf(
                "ShopName" to "Şık Tıraş",
                "Owner" to "Mehmet Demir",
                "PhoneNumber" to "0544 987 65 43",
                "Price" to "200 TL"
            ),
            mapOf(
                "ShopName" to "Kral Berber",
                "Owner" to "Ali Kaya",
                "PhoneNumber" to "0533 456 78 90",
                "Price" to "175 TL"
            ),
            mapOf(
                "ShopName" to "Kral Berber",
                "Owner" to "Ali Kaya",
                "PhoneNumber" to "0533 456 78 90",
                "Price" to "175 TL"
            ),
            mapOf(
                "ShopName" to "Kral Berber",
                "Owner" to "Ali Kaya",
                "PhoneNumber" to "0533 456 78 90",
                "Price" to "175 TL"
            ),
            mapOf(
                "ShopName" to "Kral Berber",
                "Owner" to "Ali Kaya",
                "PhoneNumber" to "0533 456 78 90",
                "Price" to "175 TL"
            ),
            mapOf(
                "ShopName" to "Kral Berber",
                "Owner" to "Ali Kaya",
                "PhoneNumber" to "0533 456 78 90",
                "Price" to "175 TL"
            ),
            mapOf(
                "ShopName" to "Kral Berber",
                "Owner" to "Ali Kaya",
                "PhoneNumber" to "0533 456 78 90",
                "Price" to "175 TL"
            ),
            mapOf(
                "ShopName" to "Kral Berber",
                "Owner" to "Ali Kaya",
                "PhoneNumber" to "0533 456 78 90",
                "Price" to "175 TL"
            ),
            mapOf(
                "ShopName" to "Kral Berber",
                "Owner" to "Ali Kaya",
                "PhoneNumber" to "0533 456 78 90",
                "Price" to "175 TL"
            ),
            mapOf(
                "ShopName" to "Kral Berber",
                "Owner" to "Ali Kaya",
                "PhoneNumber" to "0533 456 78 90",
                "Price" to "175 TL"
            ),
            mapOf(
                "ShopName" to "Kral Berber",
                "Owner" to "Ali Kaya",
                "PhoneNumber" to "0533 456 78 90",
                "Price" to "175 TL"
            ),
            mapOf(
                "ShopName" to "Kral Berber",
                "Owner" to "Ali Kaya",
                "PhoneNumber" to "0533 456 78 90",
                "Price" to "175 TL"
            ),
            mapOf(
                "ShopName" to "Kral Berber",
                "Owner" to "Ali Kaya",
                "PhoneNumber" to "0533 456 78 90",
                "Price" to "175 TL"
            ),
            mapOf(
                "ShopName" to "Kral Berber",
                "Owner" to "Ali Kaya",
                "PhoneNumber" to "0533 456 78 90",
                "Price" to "175 TL"
            )


        )

        val adapter = HomeAdapter(dummyList)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

     fun updateIconHome (clickedButton:ImageButton){
        // Bütün butonlar başlangıç ikonuna sıfırlandı
        binding.homeButton.setImageResource(R.drawable.home2)
        binding.userButton.setImageResource(R.drawable.user2)
        binding.appoinmentButton.setImageResource(R.drawable.appoinment1)

        //tıklanan butonun ikonu değişicek
        clickedButton.setImageResource(R.drawable.home)
    }
     fun updateIconUser (clickedButton:ImageButton){
        binding.homeButton.setImageResource(R.drawable.home2)
        binding.userButton.setImageResource(R.drawable.user2)
        binding.appoinmentButton.setImageResource(R.drawable.appoinment1)

        clickedButton.setImageResource(R.drawable.usericon)
    }
     fun updateIconAppoinment(clickedButton:ImageButton){
        binding.homeButton.setImageResource(R.drawable.home2)
        binding.userButton.setImageResource(R.drawable.user2)
        binding.appoinmentButton.setImageResource(R.drawable.appoinment1)

        clickedButton.setImageResource(R.drawable.appoinment2)


    }

    fun appoinmentPage(view: View){
        val action = UserMainFragmentDirections.actionUserMainFragmentToAppoinmentFragment()
        Navigation.findNavController(view).navigate(action)

    }

    fun hometouser(view: View){
        val action = UserMainFragmentDirections.actionUserMainFragmentToUserPageFragment()
        Navigation.findNavController(view).navigate(action)


    }

}