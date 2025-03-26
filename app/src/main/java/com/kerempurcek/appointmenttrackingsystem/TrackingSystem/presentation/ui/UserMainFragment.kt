package com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.ui

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.kerempurcek.appointmenttrackingsystem.R
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.adapter.HomeAdapter
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.domain.model.EditOwnerDataClass
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.viewmodal.FireStoreViewModel
import com.kerempurcek.appointmenttrackingsystem.databinding.FragmentUserMainBinding


class UserMainFragment : Fragment() {
    private var _binding: FragmentUserMainBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var  db : FirebaseFirestore
    private lateinit var auth:FirebaseAuth
    private var adapter: HomeAdapter? = null
    private lateinit var viewModel: FireStoreViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Firebase.firestore
        auth = Firebase.auth
        // Bildirim kanalı oluşturuluyor
        createNotificationChannel(requireContext())


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

        viewModel = ViewModelProvider(this)[FireStoreViewModel::class.java]

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

        // Adapter & FireStore Data
        GetFirestoreData()
        adapter = HomeAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter =adapter

        checkCancellationAndShowDialog()

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

    private fun GetFirestoreData(){
       viewModel.loading.observe(viewLifecycleOwner){isLoading->
           if(isLoading){
               binding.progressBar1.visibility = View.VISIBLE
           }else{
               binding.progressBar1.visibility = View.GONE
           }
       }
        //veriyi gözlemle
        viewModel.owners.observe(viewLifecycleOwner){getData->
            adapter?.updateList(getData)
        }
        viewModel.getFireStoreData()

    }


    // Kullanıcı randevusu iptal edilmişse, cancelappointments koleksiyonunda kontrol et
    private fun checkCancellationAndShowDialog() {
        val currentUserId = auth.currentUser?.uid
        val context = requireContext()

        // cancelappointments koleksiyonunda currentUserId ile eşleşen bir belge var mı kontrol et
        if (currentUserId != null) {
            db.collection("cancelAppointments").document(currentUserId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        // Eşleşen veri bulundu, kullanıcının randevusu iptal edilmiş
                        showCancellationDialog(context, currentUserId)
                    }
                }

        }
    }

    // Kullanıcıya AlertDialog göster
    private fun showCancellationDialog(context: Context, userId: String) {
        val dialog = MaterialAlertDialogBuilder(context, R.style.CustomAlertDialog)
            .setTitle("Randevu İptali")
            .setMessage("Randevunuz iptal edilmiştir.Lütfen berberinizle iletişime geçin veya farklı bir randevu alın!")
            .setPositiveButton("Tamam") { _, _ ->
                // Kullanıcı tamam derse, cancelappointments koleksiyonundaki veriyi sil
                deleteCancellationFromCancelAppointments(userId, context)
            }
            .setCancelable(false)

            .create()
        dialog.show()
    }

    // Kullanıcı "Tamam" dediğinde cancelappointments koleksiyonundaki veriyi sil
    private fun deleteCancellationFromCancelAppointments(userId: String, context: Context) {
        db.collection("cancelAppointments").document(userId).delete()

    }


    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "appointment_reminder_channel"
            val channelName = "Randevu Hatırlatıcı"
            val channelDescription = "Randevularınızın hatırlatılacağı bildirim kanalı"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }



}