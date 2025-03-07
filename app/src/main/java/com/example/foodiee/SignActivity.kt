package com.example.foodiee

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodiee.databinding.ActivitySignBinding
import com.example.foodiee.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

@Suppress("DEPRECATION", "NAME_SHADOWING")
class SignActivity : AppCompatActivity() {

    private lateinit var email:String
    private lateinit var password:String
    private lateinit var username:String
    private lateinit var auth:FirebaseAuth
    private lateinit var database:DatabaseReference
    private lateinit var googleSignInClint: GoogleSignInClient

    private val binding : ActivitySignBinding by lazy {
        ActivitySignBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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

        binding.createAccountButton.setOnClickListener{
            username = binding.userName.text.toString()
            email = binding.emailAddress.text.toString().trim()
            password = binding.password.text.toString().trim()

            if(email.isEmpty() || password.isBlank() || username.isBlank()){
                Toast.makeText(this, "Please fill all the Details", Toast.LENGTH_SHORT).show()
            }else{
                creatAccount(email, password)
            }
        }

        binding.alreadyhavebutton.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.googleButton.setOnClickListener{
            val signIntent = googleSignInClint.signInIntent
            launcher.launch(signIntent)
        }
    }

    // Launcher for google Sign In.
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        if(result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if(task.isSuccessful){
                val account:GoogleSignInAccount? = task.result
                val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener{
                    task ->
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
            Toast.makeText(this, "Sign In Field 😔", Toast.LENGTH_SHORT).show()
        }
    }

    private fun creatAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
            task ->
            if(task.isSuccessful){
                Toast.makeText(this, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                saveUserData()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }else{
                Toast.makeText(this, "Account Creation Failed", Toast.LENGTH_SHORT).show()
                Log.d("Account", "creatAccount: Failure", task.exception)
            }
        }
    }

    private fun saveUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val user = UserModel(username, email, password)
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
}