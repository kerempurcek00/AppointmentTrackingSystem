package com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kerempurcek.appointmenttrackingsystem.R
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.domain.model.AppointmentList
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.domain.model.EditOwnerDataClass
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.viewmodal.FireStoreViewModel
import com.kerempurcek.appointmenttrackingsystem.databinding.DeletereasonBinding
import com.kerempurcek.appointmenttrackingsystem.databinding.EditOwnerInformationBinding
import com.kerempurcek.appointmenttrackingsystem.databinding.RecyclerRowAppointmentBinding
import kotlin.coroutines.coroutineContext
import kotlin.jvm.internal.FunInterfaceConstructorReference
import kotlin.math.log



class OwnerAppointmentAdapter(var customerAppointments: MutableList<AppointmentList>) :
    RecyclerView.Adapter<OwnerAppointmentAdapter.OwnerHolder>() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    class OwnerHolder(val binding: RecyclerRowAppointmentBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OwnerHolder {
        val binding = RecyclerRowAppointmentBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return OwnerHolder(binding)
    }

    override fun getItemCount(): Int {
        return customerAppointments.size
    }

    override fun onBindViewHolder(holder: OwnerHolder, position: Int) {
        val appointment = customerAppointments[position]
        val context = holder.binding.root.context

        holder.binding.textViewCustomerName.text = appointment.nameSurname
        holder.binding.textViewAppointmentDate.text = appointment.date

        holder.binding.AppointmentCancelButton.backgroundTintList =
            ContextCompat.getColorStateList(context, R.color.backgroundtint)

        holder.binding.AppointmentCancelButton.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(context, R.style.CustomAlertDialog)
                .setTitle("Randevu İptali")
                .setMessage("Silmek istediğinize emin misiniz?")
                .setPositiveButton("Evet") { _, _ ->
                    cancelAppointmentByName(appointment.nameSurname,position, holder)
                }
                .setNegativeButton("Hayır") { dialog, _ -> dialog.dismiss() }
                .create()
            dialog.show()
        }
        holder.binding.DoneButton.setOnClickListener {
            DoneAppointment(appointment.nameSurname,holder,position)

        }
    }

    fun updateList(newList: List<AppointmentList>) {
        customerAppointments.clear()
        customerAppointments.addAll(newList)
        notifyDataSetChanged()
    }

    fun cancelAppointmentByName(nameSurname: String, position: Int, holder: OwnerHolder) {
        val db = FirebaseFirestore.getInstance()

        // "CustomerAppointments" koleksiyonunda "nameSurname" ile eşleşen veriyi buluyoruz
        db.collection("CustomerAppointments")
            .whereEqualTo("nameSurname", nameSurname) // Burada isimSoyisim ile eşleşiyor
            .get()
            .addOnSuccessListener { result ->
                // Veriyi bulduğumuzda, her eşleşen randevu için işlem yapacağız
                if (!result.isEmpty) {
                    for (document in result) {
                        val appointmentData = document.data

                        // CancelAppointments koleksiyonuna veriyi aktarırken CustomerAppointments ID'sini de ekliyoruz
                        val cancelData = hashMapOf(
                            "nameSurname" to appointmentData["nameSurname"],
                            "date" to appointmentData["date"],
                            "timestamp" to System.currentTimeMillis(),  // Zaman damgası ekliyoruz
                            "appointmentId" to document.id  // CustomerAppointments ID'sini kaydediyoruz
                        )

                        // CancelAppointments koleksiyonuna kaydediyoruz
                        db.collection("cancelAppointments")
                            .document(document.id)  // Aynı ID ile kaydediyoruz
                            .set(cancelData)
                            .addOnSuccessListener {
                                // Silme işlemine geçiyoruz, cancelAppointments'a veri ekledikten sonra
                                db.collection("CustomerAppointments").document(document.id).delete()
                                    .addOnSuccessListener {
                                        // Silme işlemi başarılı
                                        Toast.makeText(holder.binding.root.context, "Randevu başarıyla iptal edildi.", Toast.LENGTH_LONG).show()

                                        // RecyclerView'u güncellemek için
                                        customerAppointments.removeAt(position)
                                        notifyItemRemoved(position)
                                        holder.binding.AppointmentConstraintLayout.visibility = View.GONE
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(holder.binding.root.context, "Randevu silme işlemi başarısız: ${e.message}", Toast.LENGTH_LONG).show()
                                    }
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(holder.binding.root.context, "CancelAppointments'a veri eklerken hata: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                    }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(holder.binding.root.context, "Veri çekme işlemi başarısız: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }


    fun DoneAppointment(nameSurname: String,holder:OwnerHolder,position: Int){
        db.collection("CustomerAppointments").whereEqualTo("nameSurname",nameSurname).get().addOnSuccessListener { result->

            if(result!=null){
                for (document in result){
                    db.collection("DoneAppointments").document(document.id).set(document).addOnSuccessListener {
                        Toast.makeText(holder.binding.root.context,"Randevu tamamlanmıştır!",Toast.LENGTH_LONG).show()
                    }
                    db.collection("CustomerAppointments").document(document.id).delete()

                }
                // RecyclerView'u güncellemek için
                if (!customerAppointments.isNullOrEmpty() && position in customerAppointments.indices) {
                    customerAppointments.removeAt(position)
                    notifyItemRemoved(position)
                    holder.binding.AppointmentConstraintLayout.visibility = View.GONE
                }

            }
        }


    }
}

