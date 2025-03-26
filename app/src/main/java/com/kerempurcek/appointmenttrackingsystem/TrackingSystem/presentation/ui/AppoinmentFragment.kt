package com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.ui

import android.os.Bundle
import android.os.Parcel
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.transition.Visibility
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.kerempurcek.appointmenttrackingsystem.R
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.domain.model.AppointmentList
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.domain.model.DeleteAppointmentList
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.domain.model.EditOwnerDataClass
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.domain.model.GetShopNameList
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.viewmodal.FireStoreViewModel
import com.kerempurcek.appointmenttrackingsystem.databinding.FragmentAppoinmentBinding
import com.kerempurcek.appointmenttrackingsystem.databinding.FragmentUserMainBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class AppoinmentFragment : Fragment() {
    private var _binding: FragmentAppoinmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var db : FirebaseFirestore
    private lateinit var  auth : FirebaseAuth
    private lateinit var viewModel: FireStoreViewModel
    val EditList:ArrayList<GetShopNameList> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Firebase.firestore
        auth=Firebase.auth


    }

    var selectedDate: String = ""
    var selectedDateTime: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAppoinmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[FireStoreViewModel::class.java]
        ShowShopName()


        viewModel.nameSurname()
        viewModel.nameSurnameList.observe(viewLifecycleOwner){userData->
            if(userData!=null){
                binding.NameSurnameAppointment.setText(userData.nameSurname)
            }

        }


        binding.homeButton.setOnClickListener {
            homePage(it)
        }
        binding.userButton.setOnClickListener {
            hometouser(it)
        }


        binding.appoinment.setOnClickListener {
            //tarih
            datePicker()

        }




        //Randevu Oluşturmak
        binding.appoinmentRegister.setOnClickListener {
            val appointment = binding.appoinment.text.toString()
            val selectedShopName = binding.spinner2.selectedItem.toString()

            db.collection("CustomerAppointments").whereEqualTo("date",selectedDateTime).whereEqualTo("shopName",selectedShopName)
                .get()
                .addOnSuccessListener { result->

                    if(result.isEmpty){
                        if(appointment.isNotEmpty()) {
                            checkUserAppointment(it)

                        }else{
                            Toast.makeText(requireContext(),"Lütfen geçerli alanları seçiniz!",Toast.LENGTH_LONG).show()
                        }

                    }else{
                        Snackbar.make(requireView(),"Randevu saati doludur! Lütfen farklı bir randevu seçiniz",Snackbar.LENGTH_LONG).show()
                    }
                }




        }
        binding.EmailAppointment.setText(auth.currentUser?.email.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    fun homePage(view: View) {
        val action = AppoinmentFragmentDirections.actionAppoinmentFragmentToUserMainFragment()
        Navigation.findNavController(view).navigate(action)
    }

    fun hometouser(view: View){
        val action = AppoinmentFragmentDirections.actionAppoinmentFragmentToUserPageFragment()
        Navigation.findNavController(view).navigate(action)


    }

    fun checkUserAppointment(view: View){
        val currentUserEmail = auth.currentUser?.email
        var hasAppointment = false

        val appointmentDate = selectedDateTime // Kullanıcının seçtiği tarih ve saat
        val selectedShopName = binding.spinner2.selectedItem.toString()
        val userEmail = binding.EmailAppointment.text.toString()
        val userNameSurname = binding.NameSurnameAppointment.text.toString()
        // Aynı emaile ait veri kontrolü
        db.collection("CustomerAppointments").get().addOnSuccessListener { result->
            if(result!=null){
                for(document in result){
                    val email = document.get("email") as String
                    if(email==currentUserEmail){
                        hasAppointment = true
                        break   // kullanıcının randevusu var break
                    }

                }
                if(hasAppointment){
                    Toast.makeText(requireContext(),"Zaten bir randevunuz var!",Toast.LENGTH_LONG).show()
                }else{
                    createAppointment()   // kullanıcı randevusu yok randevu al fun çalışır
                    // AppointmentList nesnesi oluşturuyoruz
                    val appointmentList = AppointmentList(
                        date = appointmentDate,
                        shopName = selectedShopName,
                        email = userEmail,
                        nameSurname = userNameSurname
                    )

                    viewModel.addAppointmentAndSchedule(requireContext(), appointmentList)
                    val action = AppoinmentFragmentDirections.actionAppoinmentFragmentToApproveAppointment()
                    Navigation.findNavController(view).navigate(action)
                }
            }


        }

    }
    fun createAppointment(){
        val  AppointmentMap = hashMapOf<String,Any>()
        val selectedShop = binding.spinner2.selectedItem.toString()
        val currentUser = auth.currentUser
        val userId = currentUser?.uid
        AppointmentMap.put("date",selectedDateTime)
        AppointmentMap.put("shopName",selectedShop)
        AppointmentMap.put("email",binding.EmailAppointment.text.toString())
        AppointmentMap.put("nameSurname",binding.NameSurnameAppointment.text.toString())
        if (userId != null) {
            db.collection("CustomerAppointments").document(userId).set(AppointmentMap).addOnFailureListener{exception ->
                Toast.makeText(requireContext(),exception.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }

    }






    fun datePicker(){
        val calendar = Calendar.getInstance()
        // Bugünün tarihi
        val todayInMillis = calendar.timeInMillis // Bugünün milisaniye değeri
        calendar.add(Calendar.DAY_OF_MONTH, 1) // Yarın için bir gün ekle
        val tomorrowInMillis = calendar.timeInMillis // Yarın gününün milisaniye değeri



        // Takvimde sadece bugünü seçilemez yapacağız (bugün seçilemez)
        val constraints = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.from(todayInMillis)) // Bugünden sonrası seçilebilir
            .build()


        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Tarih Seçiniz")
            .setSelection(tomorrowInMillis)
            .setCalendarConstraints(constraints) // Bugün hariç diğer günler seçilebilir
            .setTheme(R.style.CustomDatePickerTheme)
            .build()

        datePicker.show(parentFragmentManager, "DATE_PICKER")

        //date modalından tarih seçildiğinde edittexte print et
        datePicker.addOnPositiveButtonClickListener { selection ->
            selectedDate =
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(selection))
            binding.appoinment.setText(selectedDate)


            //  saat modalı
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H) // 24 saatlik format
                .setHour(12) // Varsayılan saat
                .setTitleText("Sadece Saat Seçiniz!")
                .setMinute(0) // Varsayılan dakika
                .build()



            timePicker.show(parentFragmentManager, "TIME_PICKER")

            // TimePicker'tan saat seçildiğinde edittexte print et
            timePicker.addOnPositiveButtonClickListener {
                val hour = timePicker.hour
                val minute = 0
                // Saat ve dakikayı doğru formatta birleştirme
                val timeString = String.format("%02d:%02d", hour, minute)

                // Seçilen saati ve tarihi birleştirelim
                selectedDateTime = "$selectedDate $timeString"
                binding.appoinment.setText(selectedDateTime)

            }
        }

    }

    fun ShowShopName(){
        // ViewModel'den veriyi çekiyoruz
        viewModel.getFireStoreData()

        // ViewModel'den veriyi gözlemliyoruz
        viewModel.owners.observe(viewLifecycleOwner) { ownersList ->
            if (ownersList.isNotEmpty()) {
                // Sadece isOpen == true olanları filtrele
                val openOwners = ownersList.filter { it.isOpen }

                if (openOwners.isNotEmpty()) {
                    // Açık olan dükkanların shopName'lerini al
                    val shopNames = openOwners.map { it.shopName }

                    // Spinner adapter'ı oluştur
                    val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, shopNames)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    // Spinner'ı ayarlıyoruz
                    binding.spinner2.adapter = adapter

                    binding.LinearProgressBar.visibility = View.GONE // Veriler geldikten sonra progress bar'ı gizle
                } else {
                    // Eğer tüm dükkanlar kapalıysa bir mesaj gösterebilirsiniz
                    Log.e("AppointmentFragment", "Tüm dükkanlar kapalı!")
                    // İsterseniz bir Toast veya diğer UI öğeleri ile bilgilendirme yapabilirsiniz
                }
            } else {
                Log.e("AppointmentFragment", "Owner verisi boş!")
            }
        }


        // Eğer loading varsa, progress bar'ı göster
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.LinearProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

}