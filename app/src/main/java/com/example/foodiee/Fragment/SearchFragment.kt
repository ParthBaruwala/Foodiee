package com.example.foodiee.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodiee.R
import com.example.foodiee.adaptar.MenuAdapter
import com.example.foodiee.databinding.FragmentSearchBinding
import com.example.foodiee.databinding.MenuItemBinding

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter : MenuAdapter
    private val orignalMenuFoodName = listOf(
        "Burger",
        "sandwich",
        "momo",
        "item",
        "sandwich",
        "momo",
        "sandwich",
        "momo",
        "item",
        "sandwich",
        "momo"
    )
    private val orignalMenuItemPrice =
        listOf("200","120","100","250","120","250","120","100","250","120","250")
    private val orignalMenuImage = listOf(
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
private val filteredMenuFoodName = mutableListOf<String>()
private val filteredMenuItemPrice = mutableListOf<String>()
private val filteredMenuImage = mutableListOf<Int>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater,container,false)
        /*adapter = MenuAdapter(
            filteredMenuFoodName,
            filteredMenuItemPrice,
            filteredMenuImage,
            requireContext()
        )*/
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter = adapter

        // setup for search view
        setupSearchView()
        // show All menus Items
        showAllMenu()
        return binding.root
    }

    private fun showAllMenu() {
        filteredMenuFoodName.clear()
        filteredMenuItemPrice.clear()
        filteredMenuImage.clear()

        filteredMenuFoodName.addAll(orignalMenuFoodName)
        filteredMenuItemPrice.addAll(orignalMenuItemPrice)
        filteredMenuImage.addAll(orignalMenuImage)

        adapter.notifyDataSetChanged()
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                filterMenuItems(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterMenuItems(newText)
                return true
            }
        })
    }

    private fun filterMenuItems(query: String) {
        filteredMenuFoodName.clear()
        filteredMenuItemPrice.clear()
        filteredMenuImage.clear()

        orignalMenuFoodName.forEachIndexed { index, foodName ->
            if(foodName.contains(query, ignoreCase = true)){
                filteredMenuFoodName.add(foodName)
                filteredMenuItemPrice.add(orignalMenuItemPrice[index])
                filteredMenuImage.add(orignalMenuImage[index])
            }
        }
        adapter.notifyDataSetChanged()
    }

    companion object {

    }
}