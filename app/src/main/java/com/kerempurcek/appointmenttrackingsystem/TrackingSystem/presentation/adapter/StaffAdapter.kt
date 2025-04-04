package com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kerempurcek.appointmenttrackingsystem.R
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.domain.model.StaffInfo
import com.kerempurcek.appointmenttrackingsystem.databinding.RecycleRowStaffBinding


class StaffAdapter(var StaffList: MutableList<StaffInfo>) :
    RecyclerView.Adapter<StaffAdapter.StaffHolder>() {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    class StaffHolder(val binding: RecycleRowStaffBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaffHolder {
        val binding = RecycleRowStaffBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return StaffHolder(binding)
    }

    override fun getItemCount(): Int {
        return StaffList.size
    }


    override fun onBindViewHolder(holder: StaffHolder, position: Int) {
        val StaffInfo = StaffList[position]
        holder.binding.textViewStaffNameSurname.text = StaffInfo.staffNameSurname
        val context = holder.binding.root.context
        // Firestore'dan gelen `isChecked` değerine göre butonu güncelle
        updateToggleButtonUI(holder.binding.WorkingStatus, StaffInfo.isChecked)
        holder.binding.WorkingStatus.setOnClickListener {
            toggleStaffStatus(StaffInfo, position)

        }

        holder.binding.deleteStaffIcon.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(context, R.style.CustomAlertDialog)
                .setTitle("Personel Silme")
                .setMessage("Silmek istediğinize emin misiniz?")
                .setCancelable(false)
                .setPositiveButton("Evet") { _, _ ->
                    deleteStaff(StaffInfo,holder,position)
                }
                .setNegativeButton("Hayır") { dialog, _ -> dialog.dismiss() }
                .create()
            dialog.show()
        }



    }

    fun updateList(newList: List<StaffInfo>) {
        StaffList.clear()
        StaffList.addAll(newList)
        notifyDataSetChanged()

    }

    // 🔹 **ToggleButton UI'yi Güncelle**
    private fun updateToggleButtonUI(button: ToggleButton, isChecked: Boolean) {
        val context = button.context
        val colorRes = if (isChecked) R.color.green else R.color.red
        val text = if (isChecked) "Çalışıyor" else "Çalışmıyor"

        button.text = text
        button.setBackgroundColor(ContextCompat.getColor(context, colorRes))
    }

    val barberId = auth.currentUser?.uid as String

    // 🔹 **Firestore'daki `isChecked` değerini tersine çevirme fonksiyonu**
    private fun toggleStaffStatus(staff: StaffInfo, position: Int) {

        val staffNameSurname = staff.staffNameSurname // String olarak al

        db.collection("staffs").document(barberId)
            .collection("staffInfo")
            .whereEqualTo("staffNameSurname", staffNameSurname)
            .get()
            .addOnSuccessListener { StaffsStatus ->
                if (!StaffsStatus.isEmpty) {
                    for (StaffStatus in StaffsStatus) {
                        val docId = StaffStatus.id // Güncellenecek dökümanın ID'si
                        val newCheckedState = !staff.isChecked // Yeni durumu tersine çevir

                        // Firestore'da güncelle
                        db.collection("staffs").document(barberId)
                            .collection("staffInfo").document(docId)
                            .update("isChecked", newCheckedState)
                            .addOnSuccessListener {
                                // UI'daki veriyi güncelle
                                StaffList[position].isChecked = newCheckedState
                                notifyItemChanged(position) // RecyclerView'u güncelle
                            }

                    }
                }
            }
    }

    private fun deleteStaff(staff: StaffInfo,holder: StaffHolder,position: Int) {
        val staffNameSurname = staff.staffNameSurname // String olarak al
        val context = holder.binding.root.context
        db.collection("staffs").document(barberId).collection("staffInfo")
            .whereEqualTo("staffNameSurname", staffNameSurname).get().addOnSuccessListener {deletestaffs->

                for(deletestaff in deletestaffs){
                    db.collection("staffs").document(barberId)
                        .collection("staffInfo").document(deletestaff.id)
                        .delete().addOnSuccessListener {

                            Toast.makeText(context,"Personel Silinmiştir",Toast.LENGTH_LONG).show()
                        }

                }
                if (position >= 0 && position < StaffList.size) {   // Veri olmayınca position geçersiz değer olmaması için döngüyle kontrol sağlıyoruz
                    StaffList.removeAt(position)
                    notifyItemRemoved(position)
                    holder.binding.StaffConstraintLayout.visibility = View.GONE

                }
                holder.binding.StaffConstraintLayout.visibility = View.VISIBLE
            }

    }
}