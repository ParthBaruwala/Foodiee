package com.example.foodiee

import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.foodiee.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding :ActivityDetailsBinding

    private var foodName:String? = null
    private var foodImage:String? = null
    private var foodDescription:String? = null
    private var foodIngredient:String? = null
    private var foodPrice:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        foodName = intent.getStringExtra("MenuItemName")
        foodDescription = intent.getStringExtra("MenuItemDescription")
        foodIngredient = intent.getStringExtra("MenuItemIngredient")
        foodPrice = intent.getStringExtra("MenuItemPrice")
        foodImage = intent.getStringExtra("MenuItemImage")

        with(binding){
            detailFoodName.text = foodName
            detailDescription.text = foodDescription
            detailIngredients.text = foodIngredient
            Glide.with(this@DetailsActivity).load(Uri.parse(foodImage)).into(detailFoodImage)
        }

        binding.imageButton.setOnClickListener {
            finish()
        }
    }
}