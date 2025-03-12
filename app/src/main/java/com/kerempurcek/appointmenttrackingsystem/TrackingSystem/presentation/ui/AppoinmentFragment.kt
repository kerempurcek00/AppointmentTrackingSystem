package com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.ui

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.kerempurcek.appointmenttrackingsystem.R
import com.kerempurcek.appointmenttrackingsystem.databinding.FragmentAppoinmentBinding
import com.kerempurcek.appointmenttrackingsystem.databinding.FragmentUserMainBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class AppoinmentFragment : Fragment() {
    private var _binding: FragmentAppoinmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


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
        binding.homeButton.setOnClickListener {
            homePage(it)
        }
        binding.userButton.setOnClickListener {
            hometouser(it)
        }


        binding.appoinment.setOnClickListener {
            //tarih modalı
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Tarih Seçiniz")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
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
                    .setTitleText("Saat Seçiniz")
                    .setMinute(0) // Varsayılan dakika
                    .build()
                // TimePicker'ı göster
                timePicker.show(parentFragmentManager, "TIME_PICKER")

                // TimePicker'tan saat seçildiğinde edittexte print et
                timePicker.addOnPositiveButtonClickListener {
                    val hour = timePicker.hour
                    val minute = timePicker.minute

                    // Saat ve dakikayı doğru formatta birleştirme
                    val timeString = String.format("%02d:%02d", hour, minute)

                    // Seçilen saati ve tarihi birleştirelim
                    selectedDateTime = "$selectedDate $timeString"

                    // EditText'e yazdıralım
                    binding.appoinment.setText(selectedDateTime)
                }
            }


        }


        val spinner  = binding.spinner2

        val adapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item)
        adapter.add("Berber Seçiniz")
        adapter.add("Koaceli Gölcük Emrah Berber")
        adapter.add("İstanbul Kadir Berber")
        adapter.add("Ankara Mert Enes Hair Style")


        // Dropdown görünümü için kaynak belirliyoruz
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter


        //Randevu Oluşturmak
        binding.appoinmentRegister.setOnClickListener {
           doneMessage(it)


        }
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
    fun doneMessage(view: View){
        val action = AppoinmentFragmentDirections.actionAppoinmentFragmentToApproveAppointment()
        Navigation.findNavController(view).navigate(action)
    }

}