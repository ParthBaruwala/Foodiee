package com.example.foodiee

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.foodiee.databinding.ActivityDetailsBinding
import com.example.foodiee.model.CartItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayInputStream

@Suppress("DEPRECATION")
class DetailsActivity : AppCompatActivity() {
    private lateinit var binding :ActivityDetailsBinding

    private var foodName:String? = null
    private var foodImage:String? = null
    private var foodDescription:String? = null
    private var foodIngredient:String? = null
    private var foodPrice:String? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var progressDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

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

        binding.addItemButton.setOnClickListener {
            showProgressDialog()
            addItemToCart()
        }
    }

    private fun addItemToCart() {
        val userId = auth.currentUser?.uid

        if (userId != null) {
            val database = FirebaseDatabase.getInstance().reference

            // Create a CartItems object
            val cartItem = CartItems(
                foodName = foodName.toString(),
                foodPrice = foodPrice.toString(),
                foodDescription = foodDescription.toString(),
                foodImage = foodImage.toString(),
                1,
                foodIngredients = foodIngredient.toString()
            )

            // Save data to cart under the specific user's ID
            database.child("user").child(userId).child("CartItems").push()
                .setValue(cartItem)
                .addOnSuccessListener {
                    hideProgressDialog()
                    Toast.makeText(this, "Item added to cart successfully 😁", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    hideProgressDialog()
                    Toast.makeText(this, "Failed to add item to cart 😒", Toast.LENGTH_SHORT).show()
                }
        } else {
            hideProgressDialog()
            Toast.makeText(this, "User not logged in! Please sign in to add items to the cart.", Toast.LENGTH_SHORT).show()
        }
    }

    // Progress dialog to show loading
    private fun showProgressDialog() {
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.progress_dialog, null)

        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        builder.setCancelable(false)

        progressDialog = builder.create()
        progressDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        progressDialog.show()
    }

    private fun hideProgressDialog() {
        if (::progressDialog.isInitialized && progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }
}