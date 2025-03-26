package com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.adapter




import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.domain.model.EditOwnerDataClass
import com.kerempurcek.appointmenttrackingsystem.databinding.RecylerRowHomeBinding


class HomeAdapter(var ownerList: List<EditOwnerDataClass>): RecyclerView.Adapter<HomeAdapter.HomeHolder>() {
    
    class HomeHolder(val binding: RecylerRowHomeBinding):RecyclerView.ViewHolder(binding.root){
        
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeHolder {
       val RecyclerRowBinding = RecylerRowHomeBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return HomeHolder(RecyclerRowBinding)
    }

    override fun getItemCount(): Int {
       return ownerList.size
    }

    override fun onBindViewHolder(holder: HomeHolder, position: Int) {
        val owner = ownerList[position]
        holder.binding.apply {
            RecyclerShopName.text = owner.shopName
            RecyclerOwnerName.text = owner.ownerName
            RecyclerPhoneNumber.text = owner.ownerPhone
        }
    }

    fun updateList(newList: List<EditOwnerDataClass>) {
        ownerList = newList
        notifyDataSetChanged() // Liste değişince güncelle
    }

}