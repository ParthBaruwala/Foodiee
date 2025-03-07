@file:Suppress("DEPRECATION")

package com.example.foodiee

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.foodiee.databinding.ActivityLoginBinding
import com.example.foodiee.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

@Suppress("DEPRECATION", "NAME_SHADOWING")
class LoginActivity : AppCompatActivity() {

    private var userName:String? = null
    private lateinit var email:String
    private lateinit var password:String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClint: GoogleSignInClient
    private lateinit var progressDialog: AlertDialog

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        // initialization of Firebase auth.
        auth = FirebaseAuth.getInstance()
        // initialization of Firebase database.
        database = FirebaseDatabase.getInstance().reference
        // initialization of Google
        googleSignInClint = GoogleSignIn.getClient(this, googleSignInOption)

        binding.loginButton.setOnClickListener {
            // get data from text field.
            email = binding.EmailAddress.text.toString().trim()
            password = binding.Password.text.toString().trim()

            if(email.isBlank() || password.isBlank()){
                Toast.makeText(this, "Please enter all the Details 😒", Toast.LENGTH_SHORT).show()
            }else{
                showProgressDialog()
                createUser()
                Toast.makeText(this, "Login Successful 😁", Toast.LENGTH_SHORT).show()
            }
        }
        binding.donthavebutton.setOnClickListener {
            val intent = Intent(this,SignActivity::class.java)
            startActivity(intent)
        }

        // Google Sign-In
        binding.GoogleButton.setOnClickListener {
            showProgressDialog() // Show loading
            val signInIntent = googleSignInClint.signInIntent
            launcher.launch(signInIntent)
        }
    }

    // Launcher for Google-Sign-In.
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        if(result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if(task.isSuccessful){
                val account: GoogleSignInAccount? = task.result
                val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener{
                    task ->
                    hideProgressDialog()
                    if(task.isSuccessful){
                        Toast.makeText(this, "Sign In SuccessFull 😁", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }else{
                        Toast.makeText(this, "Sign In Field 😔", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }else{
            hideProgressDialog()
            Toast.makeText(this, "Sign In Field 😔", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createUser() {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
            task ->
            hideProgressDialog()
            if(task.isSuccessful){
                val user = auth.currentUser
                updateUi(user)
            }else{
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                    task ->
                    if(task.isSuccessful){
                        saveUserData()
                        val user = auth.currentUser
                        updateUi(user)
                    }else{
                        Toast.makeText(this, "Error 😒", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun saveUserData() {
        // get data from text field.
        email = binding.EmailAddress.text.toString().trim()
        password = binding.Password.text.toString().trim()
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val user = UserModel(userName, email, password)
            database.child("user").child(userId).setValue(user)
                .addOnSuccessListener {
                    Toast.makeText(this, "User data saved successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to save user data: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("Database", "saveUserData: Failure", e)
                }
        } else {
            Toast.makeText(this, "User ID is null. Data not saved.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun updateUi(user: FirebaseUser?) {
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
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