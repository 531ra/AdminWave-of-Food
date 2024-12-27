package com.example.adminwaveoffood

import android.content.Intent
import android.icu.text.Transliterator.Position
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminwaveoffood.adapter.PendingOrderAdapter
import com.example.adminwaveoffood.databinding.ActivityPendingOrder2Binding
import com.example.adminwaveoffood.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PendingOrderActivity2 : AppCompatActivity(),PendingOrderAdapter.OnItemClicked {
    private lateinit var binding:ActivityPendingOrder2Binding
  private lateinit var auth:FirebaseAuth
  private var listofName:MutableList<String> = mutableListOf()
    private var listofTotalPrice:MutableList<String> = mutableListOf()
    private var listofImageFirstFoodOrder:MutableList<String> = mutableListOf()
    private var listofOrderItem:ArrayList<OrderDetails> = arrayListOf()
    private lateinit var database:FirebaseDatabase
    private lateinit var databaseOrderDetails:DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pending_order2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding=ActivityPendingOrder2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageButton.setOnClickListener { finish() }

        //initialize of database
        database=FirebaseDatabase.getInstance()
        //initialize the database reference
        databaseOrderDetails=database.reference.child("OrderDetails")
        getOrderDetails()


    }

    private fun getOrderDetails() {
        //retrieve order details from firebase database
        databaseOrderDetails.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(orderSnapshot in snapshot.children){
                    val orderDetails=orderSnapshot.getValue(OrderDetails::class.java)
                    orderDetails?.let {
                         listofOrderItem?.add(it)
                    } }
                addDataToListForRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun addDataToListForRecyclerView() {
        for(orderItem in listofOrderItem){
            //add data to respective list for populating the recyclerView
             orderItem.userName?.let { listofName.add(it) }
            orderItem.totalPrice?.let { listofTotalPrice.add(it) }
            orderItem.foodImages?.filterNot{ it.isEmpty()}?.forEach{ listofImageFirstFoodOrder.add(it.toString()) }
        }
        setAdapter()

    }

    private fun setAdapter() {
        binding.pendingOrderRecyclerView.layoutManager=LinearLayoutManager(this)
        val adapter=PendingOrderAdapter(this,listofName,listofTotalPrice,listofImageFirstFoodOrder,this)
        binding.pendingOrderRecyclerView.adapter=adapter

    }

    override fun onItemClickListener(position: Int) {
        val intent= Intent(this,OrderDetailsActivity::class.java)
         val userOrderDetails=listofOrderItem[position]
        intent.putExtra("UserOrderDetails",userOrderDetails)
        startActivity(intent)

    }

    override fun onItemAcceptClickListener(position: Int) {
        //handle item acceptance and update database
        val childItemPushKey=listofOrderItem[position].itemPushKey
        val clickItemOrderReference=childItemPushKey?.let {
            database.reference.child("OrderDetails").child(it)
        }
        clickItemOrderReference?.child("orderAccepted")?.setValue(true)
        updateOrderAcceptStatus(position)

    }
    override fun onItemDispatchClickListener(position: Int) {
        //handle item Dispatch and update database

        val dispatchItemPushKey=listofOrderItem[position].itemPushKey
        val dispatchItemOrderReference=database.reference.child("CompletedOrder").child(dispatchItemPushKey!!)
        dispatchItemOrderReference.setValue(listofOrderItem[position])
            .addOnSuccessListener {
                deleteThisItemFromOrderDetails(dispatchItemPushKey)
            }


    }

    private fun deleteThisItemFromOrderDetails(dispatchItemPushKey: String) {
        val orderDetailsItemsReference=database.reference.child("OrderDetails").child(dispatchItemPushKey)
        orderDetailsItemsReference.removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Order is Dispatched", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Order is not Dispatched", Toast.LENGTH_SHORT).show()
            }

    }


    private fun updateOrderAcceptStatus(position: Int) {
        //update order acceptance in user Buy history and order details
        val userIdOfClickedItem=listofOrderItem[position].userUid
        val pushKeyofClickedItem=listofOrderItem[position].itemPushKey
        val buyHistoryReference=database.reference.child("Users").child(userIdOfClickedItem!!).child("BuyHistory").child(pushKeyofClickedItem!!)
        buyHistoryReference.child("orderAccepted").setValue(true)
        databaseOrderDetails.child(pushKeyofClickedItem).child("orderAccepted").setValue(true)

    }
}