package com.example.adminwaveoffood

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminwaveoffood.databinding.ActivityMainBinding
import com.example.adminwaveoffood.model.OrderDetails
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var completeOrderReference:DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.addmenu.isClickable=true
        binding.addmenu.setOnClickListener {
            startActivity(Intent(this,AddItemActivity2::class.java))
        }
        binding.allItemMenu.setOnClickListener {
            startActivity(Intent(this,AllItemsActivity2::class.java))
        }
        binding.outForDelivery.setOnClickListener {
            startActivity(Intent(this,OutForDeliveryActivity2::class.java))
        }
        binding.Profile.setOnClickListener {
            startActivity(Intent(this,AdminProfileActivity2::class.java))
        }
        binding.CreateUser.setOnClickListener {
            startActivity(Intent(this,CreateUserActivity2::class.java))
        }
        binding.PendingOrder.setOnClickListener {
            startActivity(Intent(this,PendingOrderActivity2::class.java))
        }
 auth=Firebase.auth
binding.logout.setOnClickListener{
    auth.signOut()
    val googleSignInOptions= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(getString(R.string.Client_Id)).requestEmail().build()
    googleSignInClient= GoogleSignIn.getClient(this,googleSignInOptions)
    googleSignInClient.signOut().addOnCompleteListener {
        // Once sign out is complete, redirect to login activity
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}

        //to get information about total number of pending orders
        pendingOrders()
        //to get information about total number of completed order
        completedOrders()
        //tell whole time earning
        wholeTimeEarning()

    }

    private fun wholeTimeEarning() {
        var  listofTotalPay= mutableListOf<Int>()

        database=FirebaseDatabase.getInstance()
        completeOrderReference=database.reference.child("CompletedOrder")

        completeOrderReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               for(ordersnapshot in snapshot.children){
                   var completeOrder=ordersnapshot.getValue(OrderDetails::class.java)
                   completeOrder?.totalPrice?.replace("₹","")?.toIntOrNull()?.let {
                       listofTotalPay.add(it)
                   }
               }
                binding.wholeTimeEarning.text=  "₹"+ listofTotalPay.sum().toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


    }

    private fun completedOrders() {
        database=FirebaseDatabase.getInstance()
        var completedOrderReference=database.reference.child("CompletedOrder")
        var completedOrderItemCount=0
        completedOrderReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                completedOrderItemCount=snapshot.childrenCount.toInt()
                binding.completedOrder.text=completedOrderItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    private fun pendingOrders() {
        database=FirebaseDatabase.getInstance()
       var pendingOrderReference=database.reference.child("OrderDetails")
        var pendingOrderItemCount=0
        pendingOrderReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                pendingOrderItemCount=snapshot.childrenCount.toInt()
                binding.pendingOrders.text=pendingOrderItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}