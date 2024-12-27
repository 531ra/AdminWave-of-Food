package com.example.adminwaveoffood

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminwaveoffood.databinding.ActivityAdminProfile2Binding
import com.example.adminwaveoffood.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminProfileActivity2 : AppCompatActivity() {
    private lateinit var binding:ActivityAdminProfile2Binding
    private lateinit var auth:FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var adminReference:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_profile2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth=FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance()
        adminReference=database.reference.child("Users")
        binding=ActivityAdminProfile2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageButton.setOnClickListener { finish() }
        binding.apply {
            name.isEnabled=false
            email.isEnabled=false
            phonenumber.isEnabled=false
            password.isEnabled=false
            address.isEnabled=false
            savebtn.isEnabled=false
        }
        var isEnable=false
        binding.editbtn.setOnClickListener {
            isEnable = !isEnable
            if (isEnable) {
                binding.apply {
                    name.isEnabled =isEnable
                    email.isEnabled = isEnable
                    phonenumber.isEnabled = isEnable
                    password.isEnabled = isEnable
                    address.isEnabled = isEnable

                }

            }
            if(isEnable){
                binding.name.requestFocus()
            }
        }
        retrevieUserData()


    }

    private fun retrevieUserData() {
        adminReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    var ownerName=snapshot.child("").getValue()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}