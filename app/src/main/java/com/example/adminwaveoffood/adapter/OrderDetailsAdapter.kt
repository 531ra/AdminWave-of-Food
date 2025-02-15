package com.example.adminwaveoffood.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminwaveoffood.databinding.OrderDetailsItemsBinding

class OrderDetailsAdapter(private var context: Context,
    private var foodNames:ArrayList<String>,
    private var foodImage:ArrayList<String>,
    private var foodQuantitys:ArrayList<Int>,
    private var foodPrices:ArrayList<String>):RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailsViewHolder {
       val binding=OrderDetailsItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return OrderDetailsViewHolder(binding)
    }

    override fun getItemCount():Int=foodNames.size

    override fun onBindViewHolder(holder: OrderDetailsViewHolder, position: Int) {
     holder.bind(position)
    }

    inner class OrderDetailsViewHolder(private val binding:OrderDetailsItemsBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            binding.apply {
                foodName.text=foodNames[position]
                foodQuantity.text=foodQuantitys[position].toString()
                val uri=Uri.parse(foodImage[position])
                Glide.with(context).load(uri).into(orderFoodImage)
                foodPrice.text=foodPrices[position]


            }

        }

    }

}