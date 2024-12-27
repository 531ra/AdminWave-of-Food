package com.example.adminwaveoffood.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminwaveoffood.databinding.ItemItemBinding
import com.example.adminwaveoffood.model.AllMenu
import com.google.firebase.database.DatabaseReference

class AllItemAdapter(
    private val context: Context,
    private val menuList: ArrayList<AllMenu>,
    databaseReference: DatabaseReference

):RecyclerView.Adapter<AllItemAdapter.AddItemViewHolder>(){

private val itemQuantity=IntArray(menuList.size){1}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddItemViewHolder {
       val  binding=ItemItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AddItemViewHolder(binding)

    }

    override fun getItemCount(): Int {
     return menuList.size
    }

    override fun onBindViewHolder(holder: AddItemViewHolder, position: Int) {
      holder.bind(position)
    }
  inner  class AddItemViewHolder(private val binding:ItemItemBinding) :RecyclerView.ViewHolder(binding.root) {
      fun bind(position: Int) {
          binding.apply {
              val quantity=itemQuantity[position]
              val menuItem=menuList[position]
              val uriString=menuItem.foodImage
              val uri= Uri.parse(uriString)

              foodNametextview.text=menuItem.foodName
              quantitytextview.text=quantity.toString()
              Glide.with(context).load(uri).into(foodImageView)

              PriceTextview.text= "₹"+menuItem.foodPrice
              minusbtn.setOnClickListener{
                  decreaseQuantity(position)
              }
              plusbtn.setOnClickListener{
                  increaseQuantity(position)
              }
              deletebtn.setOnClickListener{
                  deleteQuantity(position)
              }
          }


      }
      private fun deleteQuantity(position: Int) {
          menuList.removeAt(position)

          notifyItemRemoved(position)
          notifyItemRangeChanged(position,menuList.size)

      }

      private fun increaseQuantity(position: Int) {
          if(itemQuantity[position]<10){
              itemQuantity[position]++
              binding.quantitytextview.text=itemQuantity[position].toString()
              notifyItemChanged(position)
          }
      }

      private fun decreaseQuantity(position: Int) {
          if(itemQuantity[position]>1){
              itemQuantity[position]--
              binding.quantitytextview.text=itemQuantity[position].toString()
              notifyItemChanged(position)
          }


      }

  }


}