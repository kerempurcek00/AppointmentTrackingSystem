package com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.viewmodal

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.domain.model.AppointmentList
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.domain.model.EditOwnerDataClass
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.domain.model.ShopInfo
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.domain.model.StaffInfo
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.domain.model.UserTypes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FireStoreViewModel(application: Application) : AndroidViewModel(application) {
    private val db = FirebaseFirestore.getInstance()  // tek bir firestore nesnesi
    private val auth  = FirebaseAuth.getInstance()
    private val _staffs = MutableLiveData<List<StaffInfo>>()
    val staffs : LiveData<List<StaffInfo>> get()  = _staffs
    val _owners = MutableLiveData<List<EditOwnerDataClass>>()
    val owners: LiveData<List<EditOwnerDataClass>> = _owners // Public olarak immutable
    val _CustomerAppointments = MutableLiveData<List<AppointmentList>>()
    val CustomerAppointments:LiveData<List<AppointmentList>> = _CustomerAppointments
    val _nameSurname = MutableLiveData<UserTypes>()
    val nameSurnameList:LiveData<UserTypes> = _nameSurname
    val tempList = mutableListOf<EditOwnerDataClass>()
    val list = mutableListOf<AppointmentList>()
    private val _loading = MutableLiveData<Boolean>()
    val loading:LiveData<Boolean> get() = _loading// Public olarak immutable
    val _AppointmentLists = MutableLiveData<AppointmentList>()
    val AppointmentLists:LiveData<AppointmentList> = _AppointmentLists

    //shopInfo
    private val _shopInfo = MutableLiveData<ShopInfo>()
    val shopInfo :LiveData<ShopInfo?> get() = _shopInfo

    fun getFireStoreData(){
        _loading.value =true

        viewModelScope.launch(Dispatchers.IO){
            db.collection("OwnerInformation").get().addOnSuccessListener {result->
                if(result!=null){
                    _owners.value = listOf()// doluysa eğer liste sil
                    for(berber in result){
                        val shopName = berber.get("shopName") as String
                        val ownerName = berber.get("ownerName") as String
                        val ownerPhone = berber.get("ownerPhone") as String
                        val isOpen = berber.get("isOpen") as Boolean
                        val list = EditOwnerDataClass(shopName,ownerName,ownerPhone,isOpen)
                        tempList.add(list)

                    }

                    _owners.value = tempList // LiveData'ya yeni listeyi ata
                    _loading.value =false
                }


            }

        }


    }
    fun GetShopInfo(auth:FirebaseAuth) {
        _loading.value = true

        val currentUserId = auth.currentUser?.uid ?: return // Null güvenliği için
        db.collection("OwnerInformation").document(currentUserId)
            .addSnapshotListener() { shopInfo,e ->

                if (shopInfo != null) {
                    if (shopInfo.exists()) {
                        // Null kontrolü ile güvenli şekilde verileri al
                        val shopName = shopInfo.getString("shopName")
                            ?: "Bilinmiyor"  // Null olursa "Bilinmiyor" ata
                        val ownerPhone = shopInfo.getString("ownerPhone")
                            ?: "Bilinmiyor" // Null olursa "Bilinmiyor" ata
                        _shopInfo.value = ShopInfo(shopName, ownerPhone)  //ShopInfo modelden çekildi


                    }
                }
                _loading.value =false
            }

    }

    fun getStaffsData(auth:FirebaseAuth){
        val tempList = mutableListOf<StaffInfo>() // Geçici liste oluştur
        val currentUserId = auth.currentUser?.uid ?: return
        _loading.value = true
        db.collection("staffs").document(currentUserId).collection("staffInfo").get().addOnSuccessListener { staffsInfo ->
          if(staffsInfo!=null){
              for(staff in staffsInfo){
                  val shopname = staff.getString("shopName") as String
                  val staffNameSurname = staff.getString("staffNameSurname") as String
                  val isChecked = staff.get("isChecked") as Boolean
                  tempList.add(StaffInfo(shopname,staffNameSurname,isChecked))
              }
              _staffs.value = tempList

          }

            _loading.value =false
        }


    }

     fun nameSurname(){
         _loading.value = true
        val currentUser = auth.currentUser
        val userId = currentUser?.uid
        if (userId != null) {
            db.collection("UserTypes").document(userId).get().addOnSuccessListener {result->
                if(result!=null){
                    val nameSurname = result.get("nameSurname") as String
                    val userType = result.get("userType") as String
                    val user = UserTypes(nameSurname,userType)
                    _nameSurname.value = user


                }

                _loading.value =false

            }
        }

    }

     fun getAppointmentData() {
         db.collection("CustomerAppointments").get().addOnSuccessListener { result ->
            if (result != null) {
                for (document in result) {
                    val date = document.get("date") as String
                    val email = document.get("email") as String
                    val shopName = document.get("shopName") as String
                    val nameSurname = document.get("nameSurname") as String

                    _AppointmentLists.value = AppointmentList(date,email,shopName,nameSurname)


                }
                _loading.value = false

            }


        }
    }

     fun getCustomerAppointment() {
        val currentUserId = auth.currentUser?.uid.toString()

         _loading.value = true
        // İlk olarak o anki berberin mağaza adını al
        db.collection("OwnerInformation")
            .document(currentUserId)
            .get()
            .addOnSuccessListener { result ->
                if (result.exists()) {
                    val tempList = mutableListOf<AppointmentList>() // Geçici liste oluştur
                    val shopName1 = result.get("shopName").toString() // Berberin mağaza adı

                    // Şimdi, sadece bu mağaza adına ait randevular
                    db.collection("CustomerAppointments")
                        .whereEqualTo("shopName", shopName1) // Mağaza adı eşleşen randevuları al
                        .get()
                        .addOnSuccessListener { appointmentsResult ->
                            
                            for (documents in appointmentsResult) {
                                val date = documents.get("date") as String
                                val nameSurname = documents.get("nameSurname") as String
                                val email = documents.get("email") as String

                                // Yeni verileri listeye ekle
                                tempList.add(AppointmentList(date, email, shopName1, nameSurname)) // Listeye ekle

                            }
                            // **LiveData'yı güncelle**
                            _CustomerAppointments.value = tempList
                            _loading.value = false
                        }
                   
                }
            }

    }



}