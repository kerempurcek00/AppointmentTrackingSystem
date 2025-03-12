package com.kerempurcek.appointmenttrackingsystem.TrackingSystem.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kerempurcek.appointmenttrackingsystem.R
import com.kerempurcek.appointmenttrackingsystem.databinding.DeletereasonBinding
import com.kerempurcek.appointmenttrackingsystem.databinding.EditOwnerInformationBinding
import com.kerempurcek.appointmenttrackingsystem.databinding.RecyclerRowAppointmentBinding
import kotlin.coroutines.coroutineContext
import kotlin.math.log


class OwnerAppointmentAdapter(val appointmentList: List<List<String>>):RecyclerView.Adapter<OwnerAppointmentAdapter.OwnerHolder>() {

    class OwnerHolder(val binding:RecyclerRowAppointmentBinding):RecyclerView.ViewHolder(binding.root){

    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OwnerHolder {
      val RecyclerRowAppointmentBinding = RecyclerRowAppointmentBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return OwnerHolder(RecyclerRowAppointmentBinding)
    }

    override fun getItemCount(): Int {
       return appointmentList.size
    }

    override fun onBindViewHolder(holder: OwnerHolder, position: Int) {
        val appointmentList = appointmentList[position]
        holder.binding.textViewCustomerName.text = appointmentList[0]
        holder.binding.textViewAppointmentDate.text= appointmentList[1]
        holder.binding.textViewAppoinmentTime.text = appointmentList[2]
        // recycle daki bütün öğelerin idleri context içinde tutuluyor
        val context = holder.binding.root.context
        holder.binding.AppointmentCancelButton.backgroundTintList = ContextCompat.getColorStateList(
            context, R.color.backgroundtint)

        holder.binding.AppointmentCancelButton.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(context, R.style.CustomAlertDialog)
                .setTitle("Randevu İptali")
                .setMessage("Silmek istediğinize emin misiniz?")
                .setPositiveButton("Evet") {dialog,_ ->
                    val dialogBinding = DeletereasonBinding.inflate(LayoutInflater.from(context))
                    val dialog = MaterialAlertDialogBuilder(context, R.style.CustomAlertDialog)
                        .setTitle("İptal Etme Sebebi")
                        .setView(dialogBinding.root)
                        .setPositiveButton("Gönder") {_,_ ->


                        }
                        .create()
                    dialog.show()
                    //silmek istendiğinde row constaint layoutu gizlenicek
                    holder.binding.AppointmentConstraintLayout.visibility = View.GONE
                }
                .setNegativeButton("Hayır") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
            dialog.show()
        }
    }
}