package com.example.adminwaveoffood

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminwaveoffood.databinding.ActivityAddItem2Binding
import com.example.adminwaveoffood.databinding.ActivityMainBinding
import com.example.adminwaveoffood.model.AllMenu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class  AddItemActivity2 : AppCompatActivity() {
    //food items detail
    private lateinit var foodName:String;
    private lateinit var foodPrice:String;
    private lateinit var foodIngredients:String;
    private lateinit var foodDescription:String;
    private  var foodImageUri :Uri?=null;
    //firebase
    private lateinit var auth:FirebaseAuth;
    private lateinit var database: FirebaseDatabase;


    private lateinit var binding:ActivityAddItem2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_item2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
         binding= ActivityAddItem2Binding.inflate(layoutInflater)
    setContentView(binding.root)
        binding.imageButton.setOnClickListener {
            finish()
        }
        binding.selectimage.setOnClickListener {
            pickImage.launch("image/*")

        }

        //initialize    firebase
        auth=FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance()
        binding.AddItemButton.setOnClickListener{
            //get entered data
            foodName=binding.enterFoodName.text.toString().trim()
            foodPrice=binding.enterFoodPrice.text.toString().trim()
            foodIngredients=binding.Ingredients.text.toString().trim()
            foodDescription=binding.description.text.toString().trim()
            if(foodName.isNotEmpty()||foodPrice.isNotEmpty()||foodIngredients.isNotEmpty()||foodDescription.isNotEmpty()){
                uploadData()
                Toast.makeText(this, "Item Added Successfully", Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()

            }

        }

    }

    private fun uploadData() {
        //get a reference to the "menu" node in the database
        val menuRef:DatabaseReference=database.getReference("menu")
        //generate a unique key for the new item
        val newItemKey=menuRef.push().key
if(foodImageUri!=null){
    val storageRef=FirebaseStorage.getInstance().reference
    val imageRef=storageRef.child("Menu_Images/${newItemKey}.jpg ")
    val uploadTask=imageRef.putFile(foodImageUri!!)
    uploadTask.addOnSuccessListener {
        imageRef.downloadUrl.addOnSuccessListener {downloadUrl->
            //create a new menu item
            val newItem=AllMenu(foodName=   foodName,
                foodPrice=foodPrice,
             foodIngredients=   foodIngredients,
                foodDescription = foodDescription,
                foodImage = downloadUrl.toString())
            newItemKey?.let {key->
                menuRef.child(key).setValue(newItem).addOnSuccessListener { 
                    Toast.makeText(this, "data upload Successfully", Toast.LENGTH_SHORT).show()
                }
                    .addOnFailureListener{
                        Toast.makeText(this, "data upload failed", Toast.LENGTH_SHORT).show()
                    }
            }


        }

    }
    uploadTask.addOnFailureListener{
        Toast.makeText(this, "image upload failed", Toast.LENGTH_SHORT).show()
    }


}
        else{
    Toast.makeText(this, "please select an image", Toast.LENGTH_SHORT).show()
        }
    }

    private  val pickImage=registerForActivityResult(ActivityResultContracts.GetContent()){url->
        if(url!=null){
            binding.selectedimage.setImageURI(url)
            foodImageUri=url
        }
    }
}