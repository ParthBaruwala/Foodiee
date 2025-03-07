package com.example.foodiee

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodiee.databinding.ActivityDetailsBinding
import java.io.ByteArrayInputStream

@Suppress("DEPRECATION")
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

        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        foodName = intent.getStringExtra("MenuItemName")
        foodDescription = intent.getStringExtra("MenuItemDescription")
        foodIngredient = intent.getStringExtra("MenuItemIngredient")
        foodPrice = intent.getStringExtra("MenuItemPrice")
        foodImage = intent.getStringExtra("MenuItemImage")

        with(binding){
            detailFoodName.text = foodName
            detailDescription.text = foodDescription
            detailIngredients.text = foodIngredient

            // Decode Base64 and load image
            if (!foodImage.isNullOrEmpty()) {
                try {
                    val decodedBytes = Base64.decode(foodImage, Base64.DEFAULT)
                    val inputStream = ByteArrayInputStream(decodedBytes)
                    val bitmap = BitmapFactory.decodeStream(inputStream)

                    detailFoodImage.setImageBitmap(bitmap)  // Show image
                } catch (e: Exception) {
                    Toast.makeText(this@DetailsActivity, "Image load failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.imageButton.setOnClickListener {
            finish()
        }
    }
}