package com.kerempurcek.appointmenttrackingsystem.TrackingSystem.adapter




import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kerempurcek.appointmenttrackingsystem.databinding.RecylerRowHomeBinding


class HomeAdapter(val shoplist:List<Map<String,String>>): RecyclerView.Adapter<HomeAdapter.HomeHolder>() {
    
    class HomeHolder(val binding: RecylerRowHomeBinding):RecyclerView.ViewHolder(binding.root){
        
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeHolder {
       val RecyclerRowBinding = RecylerRowHomeBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return HomeHolder(RecyclerRowBinding)
    }

    override fun getItemCount(): Int {
       return shoplist.size
    }

    override fun onBindViewHolder(holder: HomeHolder, position: Int) {
        val shop = shoplist[position]
        holder.binding.RecyclerShopName.text = shop["ShopName"]
        holder.binding.RecyclerOwnerName.text = shop["Owner"]
        holder.binding.RecyclerPhoneNumber.text = shop["PhoneNumber"]
        holder.binding.RecylerPrice.text = shop["Price"]
    }

}