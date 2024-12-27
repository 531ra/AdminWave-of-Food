package com.example.adminwaveoffood.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminwaveoffood.PendingOrderActivity2
import com.example.adminwaveoffood.databinding.PendingOrderItemBinding

class PendingOrderAdapter(private val context:Context, private val customerNames:MutableList<String>
                          , private val quantity:MutableList<String>, private val foodImage:MutableList<String>,
                          private val itemClicked:OnItemClicked
    ):RecyclerView.Adapter<PendingOrderAdapter.PendingOrderViewHolder>() {
    interface OnItemClicked  {
        fun onItemClickListener(position: Int)
        fun onItemAcceptClickListener(position: Int)
        fun onItemDispatchClickListener(position: Int)

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingOrderViewHolder {
       val binding=PendingOrderItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PendingOrderViewHolder(binding)
    }

    override fun getItemCount(): Int=customerNames.size

    override fun onBindViewHolder(holder: PendingOrderViewHolder, position: Int) {
        if (position < customerNames.size) {
            holder.bind(position)
        }

    }
    inner class PendingOrderViewHolder(private val binding:PendingOrderItemBinding):RecyclerView.ViewHolder(binding.root) {
        private var IsAccedpted=false
        fun bind(position: Int) {
            binding.apply {

                customerName.text=customerNames[position]
                PendingOrderQuantity.text=quantity[position].removeSuffix("$")
                val uri=Uri.parse(foodImage[position])


                Glide.with(context).load(uri).into(orderFoodImage)
                orderAcceptbtn.apply {
                    if(!IsAccedpted){
                        text="Accept"
                    }
                    else{
                        text="Dispatch"
                    }

                setOnClickListener {
                        if(!IsAccedpted){
                            text="Dispatch"
                            IsAccedpted=true
                            showToast("Order is Accepted")
                            itemClicked.onItemAcceptClickListener(position)

                        }
                    else{

                            customerNames.removeAt(position)
                            quantity.removeAt(position)
                            foodImage.removeAt(position)

                        notifyItemRemoved(position)
                            notifyDataSetChanged()

                            showToast("Order is Dispatched")
                            itemClicked.onItemDispatchClickListener(position)


                        }
                }


            }
                itemView.setOnClickListener {
                         itemClicked.onItemClickListener(position)
                }

        }

    }
       private fun showToast(message:String){
            Toast.makeText(context,message,Toast.LENGTH_SHORT).show()

        }
}}