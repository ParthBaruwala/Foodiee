package com.example.foodiee

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodiee.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding :ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val foodName = intent.getStringExtra("MenuItemName")
        val foodImage = intent.getIntExtra("MenuItemImage",0)
        binding.detailFoodName.text = foodName
        binding.DetailFoodImage.setImageResource(foodImage)
        binding.imageButton.setOnClickListener {
            finish()
        }

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}