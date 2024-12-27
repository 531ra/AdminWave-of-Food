package com.example.adminwaveoffood

import android.content.Intent
import android.os.Bundle
import android.util.Patterns;
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminwaveoffood.databinding.ActivitySignUp2Binding
import com.example.adminwaveoffood.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class SignUpActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivitySignUp2Binding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var email:String
    private lateinit var password:String
    private lateinit var userName:String
    private lateinit var nameofRestaurant:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding=ActivitySignUp2Binding.inflate(layoutInflater)
        setContentView(binding.root)
    val locationlist= arrayOf("jaipur","haryana","delhi","Bombay")
        val adapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,locationlist)
        val autoCompleteTextView=    binding.listofLocation
        autoCompleteTextView.setAdapter(adapter)

        // if already have account then it redirect to login activity
        binding.haveaccount.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()

        }
        // initialize firebase
        auth=Firebase.auth
        //initialize database
        database=Firebase.database.reference
        binding.createAcount.setOnClickListener {
            //get text from edit text field
            email=binding.email.text.toString()
            password=binding.password.text.toString()
            userName=binding.name.text.toString()
            nameofRestaurant=binding.restaurantname.text.toString()

            //check if email and password are not empty
            if (email.isNotEmpty() && password.isNotEmpty() && userName.isNotEmpty() && nameofRestaurant.isNotEmpty()){
                //if() block check the pattern of entered email
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(this, "Enter valid Email Address", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                    // Handle invalid email case
                 }
                else{
                    createAccount(email,password)
                }
            }
            else{
                Toast.makeText(this, "PLease enter all the fields", Toast.LENGTH_SHORT).show()
            }
        } }

    private fun createAccount(email: String, password: String) {

        //auth is used to create user account account using email and password
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { //task is the lambda function
            task->
            if(task.isSuccessful){
                // if account create successfull-> then save the user credential in the database
                saveUserData(email,password,userName,nameofRestaurant,database)
                Toast.makeText(this, "Account created", Toast.LENGTH_SHORT).show()

                            startActivity(Intent(this,LoginActivity::class.java))
                            finish()
                            // Redirect to login or close signup activity
                            // You can implement your login flow here
                        } else {
                            Toast.makeText(
                                this,
                                "Account not created",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }



        }


// save data in realtime database
    private fun saveUserData(
    email: String,
    password: String,
    userName: String,
    nameofRestaurant: String,
    database: DatabaseReference
) {

      val user=UserModel(userName,nameofRestaurant,email,password)
    // userId-> unique id created for every new user
        val userId:String=FirebaseAuth.getInstance().currentUser!!.uid
    // users is the root node and userId -> child node
        database.child("Users").child(userId).setValue(user)

    }
}