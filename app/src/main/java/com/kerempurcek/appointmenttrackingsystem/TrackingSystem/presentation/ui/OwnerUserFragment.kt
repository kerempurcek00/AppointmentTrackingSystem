package com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.ui

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.kerempurcek.appointmenttrackingsystem.R
import com.kerempurcek.appointmenttrackingsystem.R.drawable.buttondesign
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.viewmodal.FireStoreViewModel
import com.kerempurcek.appointmenttrackingsystem.databinding.EditOwnerInformationBinding
import com.kerempurcek.appointmenttrackingsystem.databinding.FragmentBarberMainBinding
import com.kerempurcek.appointmenttrackingsystem.databinding.FragmentOwnerUserBinding


class OwnerUserFragment : Fragment() {
    private var _binding: FragmentOwnerUserBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var viewModel: FireStoreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        db = Firebase.firestore


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOwnerUserBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    var infoText = "\n" +
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
        viewModel = ViewModelProvider(this)[FireStoreViewModel::class.java]

        getShopInfo()
        nameSurname()



        binding.homeButton.setOnClickListener {
            ownerUsertoHomepage(it)
        }
        binding.appoinmentButton.setOnClickListener {
            ownerUsertoAppointment(it)
        }
        binding.Logout.setOnClickListener {
            auth.signOut()
            val action = OwnerUserFragmentDirections.actionOwnerUserFragmentToLoginPage()
            Navigation.findNavController(view).navigate(action)
        }
        toggleShopStatus()
        binding.IsOpenButton.setOnClickListener {
            updateButton()
        }

        val dialogBinding =
            EditOwnerInformationBinding.inflate(LayoutInflater.from(requireContext()))

        binding.EditButton.setOnClickListener {

            val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
                .setTitle("Dükkan Bilgilerini Düzenle")
                .setView(dialogBinding.root)
                .setPositiveButton("Kaydet") { _, _ ->
                    val updatedShopName = dialogBinding.EditTextShopName.text.toString()
                    val updatedOwnerPhone = dialogBinding.EditTextOwnerPhone.text.toString()


                    // FireStore Kaydet
                    val EditMap = hashMapOf<String, Any>()
                    EditMap.put("shopName", updatedShopName)
                    EditMap.put("ownerPhone", updatedOwnerPhone)
                    EditMap.put("ownerName", binding.textViewOwnerName.text)
                    EditMap.put("isOpen",true)
                    val currentUser = auth.currentUser
                    val UserId = currentUser?.uid
                    if (UserId != null) {
                        db.collection("OwnerInformation").document(UserId).set(EditMap)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    requireContext(),
                                    "Bilgiler başarıyla kaydedilmiştir!",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                    }

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
                .setPositiveButton("Anladım") { dialog, _ ->    //_ ->kullanılmayan parametre anlamına gelir
                    // Onaylandığında yapılacak işlemler
                    dialog.dismiss()
                }
                .show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun ownerUsertoHomepage(view: View) {
        val action = OwnerUserFragmentDirections.actionOwnerUserFragmentToBarberMainFragment()
        Navigation.findNavController(view).navigate(action)

    }

    fun ownerUsertoAppointment(view: View) {
        val action = OwnerUserFragmentDirections.actionOwnerUserFragmentToOwnerAppointmentFragment()
        Navigation.findNavController(view).navigate(action)
    }

    private fun nameSurname() {
        viewModel.loading.observe(viewLifecycleOwner){isloading->
            if(isloading){
                binding.LinearProgressBarOwnerUser.visibility = View.VISIBLE
            }else{
                binding.LinearProgressBarOwnerUser.visibility = View.GONE
            }
        }
        viewModel.nameSurname()
        viewModel.nameSurnameList.observe(viewLifecycleOwner){userData->

            if(userData!=null){

                binding.textViewOwnerName.setText(userData.nameSurname)
            }
        }

    }

    private fun getShopInfo(){
        // Veriyi çekme işlemi
        viewModel.GetShopInfo(auth)
        //LiveData gözlemi
        viewModel.shopInfo.observe(viewLifecycleOwner) { shopInfo ->
            if (shopInfo != null) {
                binding.textViewShopName.text = shopInfo.shopName
                binding.textViewOwnerPhone.text = shopInfo.ownerPhone
                binding.LinearProgressBarOwnerUser.visibility = View.GONE
            }

        }
    }





    private fun toggleShopStatus() {
        val barberId = auth.currentUser?.uid // Berberin ID'si
        val shopRef = db.collection("OwnerInformation").document(barberId.toString())

        // Firestore'dan mevcut durumu çek
        shopRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                // 'isOpen' durumunu al, eğer yoksa null döner
                val isOpen = document.getBoolean("isOpen")
                if (isOpen!=null){
                    updateButtonUI(isOpen)
                }
            }
        }
    }
    private fun updateButton(){
        val barberId = auth.currentUser?.uid // Berberin ID'si
        val shopRef = db.collection("OwnerInformation").document(barberId.toString())

        // Firestore'dan mevcut durumu çek
        shopRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                // 'isOpen' durumunu al, eğer yoksa null döner
                val isOpen = document.getBoolean("isOpen")

                if (isOpen != null) {
                    // Butonun başlangıç durumunu ayarla
                    updateButtonUIX(isOpen)

                    // Tıklama sonrası 'isOpen' değerini tersine çevir ve Firestore'a kaydet
                    shopRef.update("isOpen", !isOpen) // Durumu tersine çevir
                        .addOnSuccessListener {
                            // Başarılı güncelleme sonrası kullanıcıya bilgi ver
                            Toast.makeText(requireContext(),
                                if (isOpen) "Dükkan kapandı!" else "Dükkan açıldı!",
                                Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(requireContext(), "Hata: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // Firestore'da 'isOpen' yoksa bir şey yapabiliriz ya da hata mesajı verebiliriz
                    Toast.makeText(requireContext(), "Dükkan durumu alınamadı!", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }
    private fun updateButtonUI(isOpen: Boolean) {
        // Eğer 'isOpen' true ise buton "Dükkanı Kapat" olacak, false ise "Dükkanı Aç"
        binding.IsOpenButton.text = if (isOpen) "Açık" else "Kapalı"

        // Arka plan rengini güncelle
        val backgroundColor = if (isOpen) Color.parseColor("#4CAF50") else Color.parseColor("#F44336") // Yeşil / Kırmızı
        val textColor = Color.parseColor("#FFFFFF") // Beyaz yazı rengi

        // Butonun arka plan ve yazı rengini uygula

        binding.IsOpenButton.setBackgroundColor(backgroundColor)
        binding.IsOpenButton.setTextColor(textColor)

    }
    private fun updateButtonUIX(isOpen: Boolean) {
        // Eğer 'isOpen' true ise buton "Dükkanı Kapat" olacak, false ise "Dükkanı Aç"
        binding.IsOpenButton.text = if (isOpen) "Kapalı" else "Açık"

        // Arka plan rengini güncelle
        val backgroundColor = if (isOpen) Color.parseColor("#F44336") else Color.parseColor("#4CAF50") // Yeşil / Kırmızı
        val textColor = Color.parseColor("#FFFFFF") // Beyaz yazı rengi

        // Butonun arka plan ve yazı rengini uygula


        binding.IsOpenButton.setBackgroundColor(backgroundColor)
        binding.IsOpenButton.setTextColor(textColor)




    }




}