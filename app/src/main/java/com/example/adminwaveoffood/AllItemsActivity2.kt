package com.example.adminwaveoffood

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminwaveoffood.adapter.AllItemAdapter
import com.example.adminwaveoffood.databinding.ActivityAllItems2Binding
import com.example.adminwaveoffood.model.AllMenu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList

class AllItemsActivity2 : AppCompatActivity() {
    private lateinit var adapter:AllItemAdapter
    private lateinit var binding:ActivityAllItems2Binding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private  var menuItems: ArrayList<AllMenu> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_all_items2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding= ActivityAllItems2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageButton.setOnClickListener { finish() }


        databaseReference=FirebaseDatabase.getInstance().reference

        retrieveMenuItem() }

    private fun retrieveMenuItem() {
        database=FirebaseDatabase.getInstance()
        val foodRef:DatabaseReference=database.reference.child("menu")
        //fetch data from database
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //clear existing data before populating
                menuItems.clear()
                //loop for through each food item
                for(foodSnapshot in snapshot.children){
                    val menuItem =foodSnapshot.getValue(AllMenu::class.java)
                    menuItem?.let {
                        menuItems.add(it)
                    }

                }
                setAdapter()

            }

            override fun onCancelled(error: DatabaseError) {
              Log.d("DatabaseError","Error fetching data from database: ${error.message}")
            }


        })

    }

    private fun setAdapter() {
        adapter=AllItemAdapter(this@AllItemsActivity2,menuItems,databaseReference)
        binding.MenuRecyclerView.layoutManager= LinearLayoutManager(this)
        binding.MenuRecyclerView.adapter=adapter
    }
}