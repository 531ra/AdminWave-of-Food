package com.example.adminwaveoffood

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminwaveoffood.adapter.OrderDetailsAdapter
import com.example.adminwaveoffood.databinding.ActivityOrderDetailsBinding
import com.example.adminwaveoffood.model.OrderDetails

class OrderDetailsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityOrderDetailsBinding
    private var userName:String?=null
    private var Address:String?=null
    private var phoneNumber:String?=null
    private var totalPrice:String?=null
    private var foodNames:ArrayList<String> = arrayListOf()
    private var foodImages:ArrayList<String> = arrayListOf()
    private var foodQuantity:ArrayList<Int> = arrayListOf()
    private var foodPrices:ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_order_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding=ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //for back button
        binding.backBtn.setOnClickListener {
            finish()
        }
        //function to retrieve data from intent
        getDataFromIntent()
    }

    private fun getDataFromIntent() {
            val receivedOrderDetails =intent.getSerializableExtra("UserOrderDetails")  as OrderDetails
        receivedOrderDetails?.let {orderDetails ->

                userName=receivedOrderDetails.userName
                foodNames=receivedOrderDetails.foodNames as ArrayList<String>
                foodImages=receivedOrderDetails.foodImages as ArrayList<String>
                foodQuantity=receivedOrderDetails.foodQuantities as ArrayList<Int>
                foodPrices=receivedOrderDetails.foodPrices as ArrayList<String>
                Address=receivedOrderDetails.address
                phoneNumber=receivedOrderDetails.phoneNumber
                totalPrice=receivedOrderDetails.totalPrice

                //set the information into activity
                setUserDetails()
                setAdapter()


        }


    }

    private fun setAdapter() {
        binding.orderDetailRecyclerView.layoutManager=LinearLayoutManager(this)
        val adapter=OrderDetailsAdapter(this,foodNames,foodImages,foodQuantity,foodPrices)
        binding.orderDetailRecyclerView.adapter=adapter

    }

    private fun setUserDetails() {
        binding.name.text=userName
        binding.Address.text=Address
        binding.totalPay.text=totalPrice

        binding.phone.text=phoneNumber

    }
}