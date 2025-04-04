package com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.kerempurcek.appointmenttrackingsystem.R
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.domain.model.AppointmentList
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.domain.model.StaffInfo
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.adapter.OwnerAppointmentAdapter
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.adapter.StaffAdapter
import com.kerempurcek.appointmenttrackingsystem.TrackingSystem.presentation.viewmodal.FireStoreViewModel
import com.kerempurcek.appointmenttrackingsystem.databinding.FragmentOwnerStaffBinding
import com.kerempurcek.appointmenttrackingsystem.databinding.FragmentOwnerUserBinding


class OwnerStaffFragment : Fragment() {

    private var _binding: FragmentOwnerStaffBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewModel:FireStoreViewModel
    private lateinit var auth:FirebaseAuth
    private lateinit var  db : FirebaseFirestore

    private  var adapter: StaffAdapter? = null
    val StaffListRecycleView:ArrayList<StaffInfo> = arrayListOf()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        db = Firebase.firestore

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOwnerStaffBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[FireStoreViewModel::class.java]
        getShopName()

        binding.addButton.setOnClickListener {
            AddStaffName()
        }

        // RecyclerView için adapter'ı burada tanımlıyoruz
        adapter = StaffAdapter(StaffListRecycleView)
        binding.RecycleViewStaff.layoutManager = LinearLayoutManager(requireContext())
        binding.RecycleViewStaff.adapter = adapter

        binding.SwipeRefreshLayout.setOnRefreshListener {
            binding.SwipeRefreshLayout.isRefreshing = true
            getStaffs()
            binding.SwipeRefreshLayout.isRefreshing = false
        }
        getStaffs()
        binding.BackButton.setOnClickListener {
            backUser(it)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    private fun getShopName(){

        viewModel.GetShopInfo(auth)
        viewModel.shopInfo.observe(viewLifecycleOwner){shopName ->
            if(shopName!=null){
                binding.textViewStaffShopName.text = shopName.shopName

            }

        }

    }

    private fun AddStaffName(){
        val currentUser = auth.currentUser
        val UserId = currentUser?.uid
        val shopName = binding.textViewStaffShopName.text.toString()
        val staffNameSurname = binding.staffNameSurname.text.toString()
        val createMap = hashMapOf<String,Any>()
        createMap.put("shopName",shopName)
        createMap.put("staffNameSurname",staffNameSurname)
        createMap.put("isChecked",true)

            if (staffNameSurname.isNotEmpty()){
                db.collection("staffs").document(UserId!!).collection("staffInfo").add(createMap).addOnSuccessListener {
                    binding.staffNameSurname.text?.clear()
                    Toast.makeText(requireContext(),"Çalışan Eklendi",Toast.LENGTH_LONG).show()
                }

            }else{
                Toast.makeText(requireContext(),"Lütfen Çalışan İsmi Giriniz",Toast.LENGTH_LONG).show()

            }


    }
    private fun getStaffs() {
        viewModel.loading.observe(viewLifecycleOwner){loading->
            if (loading){
                binding.LinearProgressStaff.visibility =View.VISIBLE
            }else{
                binding.LinearProgressStaff.visibility = View.GONE
            }

        }

        viewModel.staffs.observe(viewLifecycleOwner) { list ->
            adapter?.updateList(list) // Adapter'a yeni listeyi ver
        }
        // Firestore'dan veri çek
        viewModel.getStaffsData(auth)


    }
    private fun backUser(view:View){
        val action = OwnerStaffFragmentDirections.actionOwnerStaffFragmentToOwnerUserFragment()
        Navigation.findNavController(view).navigate(action)

    }

}