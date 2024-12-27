package com.example.adminwaveoffood

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminwaveoffood.adapter.DeliveryAdapter
import com.example.adminwaveoffood.databinding.ActivityOutForDelivery2Binding
import com.example.adminwaveoffood.model.OrderDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Objects

class OutForDeliveryActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityOutForDelivery2Binding
    private  lateinit var adapter:DeliveryAdapter
    private lateinit var database: FirebaseDatabase
    private  var    listofCompleteOrderList:ArrayList<OrderDetails> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_out_for_delivery2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding= ActivityOutForDelivery2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageButton.setOnClickListener { finish() }

        //retrieve and display complete order
        retrieveCompleteOrderDetail()
    }

    private fun retrieveCompleteOrderDetail() {
        //initialise database
        database=FirebaseDatabase.getInstance()
         val completeOrderReference=database.reference.child("CompletedOrder")
            .orderByChild("currentTime")
            completeOrderReference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    //clear the list before populating it with new data
                    listofCompleteOrderList.clear()
                    for(orderSnapshot in snapshot.children){
                        val completeOrder=orderSnapshot.getValue(OrderDetails::class.java)
                        completeOrder?.let { listofCompleteOrderList.add(it) }
                    }
                    //reverse the list to display latest order first
                    listofCompleteOrderList.reverse()
                    setDataIntoRecyclerView()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

    }

    private fun setDataIntoRecyclerView() {
        //initialize list to hold customer name and payment status
        val customerName= mutableListOf<String>()
        val moneyStatus= mutableListOf<Boolean>()

        for(order in listofCompleteOrderList){
            order.userName?.let { customerName.add(it) }
            moneyStatus.add(order.paymentReceived!!)
        }
        binding.deliveryRecyclerView.layoutManager=LinearLayoutManager(this)
        adapter=DeliveryAdapter(customerName,moneyStatus)
        binding.deliveryRecyclerView.adapter=adapter
    }
}