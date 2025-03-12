package com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.Navigation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kerempurcek.appointmenttrackingsystem.R
import com.kerempurcek.appointmenttrackingsystem.databinding.EditOwnerInformationBinding
import com.kerempurcek.appointmenttrackingsystem.databinding.FragmentBarberMainBinding
import com.kerempurcek.appointmenttrackingsystem.databinding.FragmentOwnerUserBinding


class OwnerUserFragment : Fragment() {
    private var _binding: FragmentOwnerUserBinding? = null

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
        _binding = FragmentOwnerUserBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    var infoText ="\n" +
            "\n" +
            "Değerli Berber Dükkanı Sahipleri,  \n" +
            "\n" +
            "Berber randevu uygulamamıza işletmenizi ekleyerek müşterilerinize daha kolay ulaşabilirsiniz. Ancak, platformun düzenli ve güvenilir bir şekilde işlemesi için aşağıdaki kurallara uymanız gerekmektedir.  \n" +
            "\n" +
            "1. İşletme Bilgileri \n" +
            "- İşletmenizin adı, adresi ve iletişim bilgileri eksiksiz ve doğru girilmelidir.  \n" +
            "- Çalışma saatleri güncel tutulmalı ve değişiklikler anında sisteme yansıtılmalıdır.  \n" +
            "- Hizmetleriniz ve fiyatlarınız açık ve net bir şekilde belirtilmelidir.  \n" +
            "\n" +
            "2. Randevu Kuralları\n" +
            "- Randevulara zamanında uyulmalı, gecikme durumunda müşteriye önceden haber verilmelidir.  \n" +
            "- Randevu iptalleri en az 24 saat önceden müşteriye bildirilmelidir.  \n" +
            "- Müşterilerin mağdur olmaması için randevu saatlerine sadık kalınmalıdır.  \n" +
            "\n" +
            "3. Müşteri Memnuniyeti ve İletişim \n" +
            "- Müşterilere karşı saygılı ve profesyonel bir tutum sergilenmelidir.  \n" +
            "- Gelen yorum ve puanlamalar dikkate alınmalı, olumsuz geri bildirimler için çözüm üretilmelidir.  \n" +
            "- Hizmet kalitenizi artırmak için müşteri önerilerini değerlendirebilirsiniz.  \n" +
            " 4. Platform Kullanımı\n" +
            "- Uygulamada yanıltıcı veya yanlış bilgi paylaşmak yasaktır.  \n" +
            "- Reklam ve promosyonlar için platformun sunduğu imkanlar kullanılabilir.  \n" +
            "- Platforma uygun olmayan içerik veya davranışlar tespit edilirse işletme sistemden kaldırılabilir.  \n" +
            "\n" +
            "Bu kurallar, sistemin sağlıklı bir şekilde işlemesi ve hem işletme sahiplerinin hem de müşterilerin memnuniyetini sağlamak için belirlenmiştir.  \n" +
            "\n" +
            "Teşekkürler,  \n" +
            "Berber Randevu Uygulaması Ekibi"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.homeButton.setOnClickListener {
            ownerUsertoHomepage(it)
        }
        binding.appoinmentButton.setOnClickListener {
            ownerUsertoAppointment(it)
        }

        val dialogBinding = EditOwnerInformationBinding.inflate(LayoutInflater.from(requireContext()))

        binding.EditButton.setOnClickListener {

            val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
                .setTitle("Dükkan Bilgilerini Düzenle")
                .setView(dialogBinding.root)
                .setPositiveButton("Kaydet") {_,_ ->
                    val updatedShopName = dialogBinding.EditTextShopName.text.toString()
                    val updatedOwnerName = dialogBinding.EditTextOwnerName.text.toString()
                    val updatedOwnerPhone = dialogBinding.EditTextOwnerPhone.text.toString()

                    binding.textViewShopName.text =updatedShopName
                    binding.textViewOwnerName.text = updatedOwnerName
                    binding.textViewOwnerPhone.text = updatedOwnerPhone
                }
                .setNegativeButton("İptal") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
            dialog.show()

        }




        binding.infoButton.setOnClickListener {
            MaterialAlertDialogBuilder(
                requireContext(),
                R.style.CustomAlertDialog
            )
                .setTitle("Bilgilendirme") // Başlık ekleyebilirsin
                .setMessage(infoText)
                .setPositiveButton("Anladım") { dialog,_ ->    //_ ->kullanılmayan parametre anlamına gelir
                    // Onaylandığında yapılacak işlemler
                    dialog.dismiss()
                }
                .show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    fun ownerUsertoHomepage(view: View) {
        val action = OwnerUserFragmentDirections.actionOwnerUserFragmentToBarberMainFragment()
        Navigation.findNavController(view).navigate(action)

    }
    fun ownerUsertoAppointment(view: View){
        val action = OwnerUserFragmentDirections.actionOwnerUserFragmentToOwnerAppointmentFragment()
        Navigation.findNavController(view).navigate(action)
    }

}