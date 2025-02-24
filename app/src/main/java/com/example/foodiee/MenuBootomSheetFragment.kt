package com.example.foodiee

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodiee.adaptar.MenuAdapter
import com.example.foodiee.databinding.FragmentMenuBootomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MenuBootomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding:FragmentMenuBootomSheetBinding

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
        val menuFoodName = listOf("Burger","sandwich","momo","item","sandwich","momo","sandwich","momo","item","sandwich","momo")
        val menuItemPrice = listOf("200","120","100","250","120","250","120","100","250","120","250")
        val menuImage = listOf(
            R.drawable.menu1,
            R.drawable.menu2,
            R.drawable.menu3,
            R.drawable.menu4,
            R.drawable.menu2,
            R.drawable.menu3,
            R.drawable.menu2,
            R.drawable.menu3,
            R.drawable.menu4,
            R.drawable.menu2,
            R.drawable.menu3
        )
        val adapter = MenuAdapter(
            ArrayList(menuFoodName),
            ArrayList(menuItemPrice),
            ArrayList(menuImage), requireContext()
        )
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter = adapter
        return binding.root
    }

    companion object {

    }
}