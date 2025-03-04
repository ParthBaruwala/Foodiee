package com.example.foodiee.adaptar

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodiee.DetailsActivity
import com.example.foodiee.databinding.MenuItemBinding
import com.example.foodiee.model.MenuItems

class MenuAdapter(
    private val menuItems:List<MenuItems>,
    private val requireContext: Context
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuItems.size

    inner class MenuViewHolder(private val binding: MenuItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION){
                    openDetailActivity(position)
                }
            }
        }

        private fun openDetailActivity(position: Int) {
            val menuItem = menuItems[position]

            // A Intent to open details activity and pass data.
            val intent = Intent(requireContext, DetailsActivity::class.java).apply {
                putExtra("MenuItemName", menuItem.foodName)
                putExtra("MenuItemImage", menuItem.foodImage)
                putExtra("MenuItemDescription", menuItem.foodDescription)
                putExtra("MenuItemIngredient", menuItem.foodIngredient)
                putExtra("MenuItemPrice", menuItem.foodPrice)
            }

            // Start Detail Activity.
            requireContext.startActivity(intent)
        }

        // set Data into recyclerview items name, price, image
        fun bind(position: Int) {
            val menuItem = menuItems[position]
            binding.apply {
                menuFoodName.text = menuItem.foodName
                menuPrice.text = menuItem.foodPrice
                val uri = Uri.parse(menuItem.foodImage)
                Glide.with(requireContext).load(uri).into(menuImage)
            }
        }
    }
}
