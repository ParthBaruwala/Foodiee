package com.example.foodiee

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodiee.adaptar.MenuAdapter
import com.example.foodiee.databinding.FragmentMenuBootomSheetBinding
import com.example.foodiee.model.MenuItems
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MenuBootomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding:FragmentMenuBootomSheetBinding

    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems: MutableList<MenuItems>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBootomSheetBinding.inflate(inflater,container,false)

        binding.buttonBack.setOnClickListener {
            dismiss()
        }

        retrieveMenuItems()

        return binding.root
    }

    private fun retrieveMenuItems() {

        // Show progress bar
        binding.progressBar.visibility = View.VISIBLE

        database = FirebaseDatabase.getInstance()
        val foodRef: DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()

        foodRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(foodSnapshot in snapshot.children){
                    val menuItem = foodSnapshot.getValue(MenuItems::class.java)
                    menuItem?.let { menuItems.add(it) }
                }

                // Hide progress bar when data is loaded
                binding.progressBar.visibility = View.GONE

                Log.d("ITEMS", "Data Received")
                // Once data receive, set to adapter.
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun setAdapter() {
        if(menuItems.isNotEmpty()){
            val adapter = MenuAdapter(menuItems, requireContext())
            binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.menuRecyclerView.adapter = adapter

            Log.d("ITEMS", "setAdapter: data set")
        }else{
            Log.d("ITEMS", "setAdapter: data not set")
        }
    }

    companion object {

    }
}