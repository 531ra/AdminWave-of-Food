package com.example.adminwaveoffood

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminwaveoffood.databinding.ActivityLoginBinding
import com.example.adminwaveoffood.databinding.ActivitySignUp2Binding
import com.example.adminwaveoffood.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var auth: FirebaseAuth  // auth is the object for firebase authentication
    private lateinit var database: DatabaseReference //instance for database reference
    private lateinit var email:String
    private lateinit  var  password:String
    private  var username:String?=null
    private  var restuarantname:String?=null
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textView10.setOnClickListener { startActivity(Intent(this,SignUpActivity2::class.java))
        finish()}
        auth= Firebase.auth
        database=Firebase.database.reference
        //initialize google sign in
        val googleSignInOptions=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.Client_Id)).requestEmail().build()
        googleSignInClient= GoogleSignIn.getClient(this,googleSignInOptions)
        binding.GoogleBtn.setOnClickListener{
            val signInIntent=googleSignInClient.signInIntent
            launhcer.launch(signInIntent)
        }

        binding.loginbtn.setOnClickListener {
            //initialize the text field
            email=binding.Email.text.toString()
            password=binding.Password.text.toString()
            //check if the text field is empty
            if(email.isNotEmpty() && password!!.isNotEmpty()){
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task->
                    if(task.isSuccessful){


                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }else{auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task->
                        if(task.isSuccessful){
                            val user= UserModel(username, restuarantname,email,password)
                            val userId:String=FirebaseAuth.getInstance().currentUser!!.uid
                            database.child("Users").child(userId).setValue(user).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this, "User saved to database", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this, "Failed to save user: ${task.exception.toString()}", Toast.LENGTH_SHORT).show()
                                }
                            }
                            startActivity(Intent(this,MainActivity::class.java))
                            finish()

                        }else{
                            Toast.makeText(this, "Login-Failed", Toast.LENGTH_SHORT).show()
                        }
                    }



                    }


                }

            }else{
                Toast.makeText(this, "enter the detail", Toast.LENGTH_SHORT).show()
            }
    }

    }

    //if user is already logged in
    override fun onStart() {
        super.onStart()
        if(auth.currentUser!=null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
    private val launhcer=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
            if(result.resultCode==Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account: GoogleSignInAccount = task.result
                    val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
                    auth.signInWithCredential(credentials).addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Succesfully Sign In with Google",
                                Toast.LENGTH_SHORT
                            ).show()

                             val email=auth.currentUser!!.email.toString()
                            val username=auth.currentUser!!.displayName.toString()

                            val user= UserModel(username, restuarantname,email,null)
                            val userId:String=FirebaseAuth.getInstance().currentUser!!.uid
                            database.child("Users").child(userId).setValue(user).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this, "User saved to database", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this, "Failed to save user: ${task.exception.toString()}", Toast.LENGTH_SHORT).show()
                                }
                            }


                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                "Sign in failed:${authTask.exception.toString()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                }
                else{
                    Toast.makeText(this, "Sign in failed:${task.exception.toString()}", Toast.LENGTH_SHORT).show()
                }

            }}
    }
