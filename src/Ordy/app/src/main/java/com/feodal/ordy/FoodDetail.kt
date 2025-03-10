package com.feodal.ordy

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.feodal.ordy.helpers.JSONHelper
import com.feodal.ordy.models.Cart
import com.feodal.ordy.models.Category
import com.feodal.ordy.models.Food
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FoodDetail : AppCompatActivity() {
    companion object {
        var ID = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_food_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val mainPhoto = findViewById<ImageView>(R.id.mainPhoto)
        val foodMainName = findViewById<TextView>(R.id.mainName)
        val foodPrice = findViewById<TextView>(R.id.foodPrice)
        val foodFullName = findViewById<TextView>(R.id.foodFullName)

        val database = FirebaseDatabase.getInstance("https://ordy-f431d-default-rtdb.europe-west1.firebasedatabase.app/")
        val table = database.getReference("Category")

        table.child(ID.toString()).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val category = snapshot.getValue(Category::class.java)

                foodMainName.text = category!!.name

                val id = when(category.image) {
                    "burger" -> R.drawable.burger
                    "chicken" -> R.drawable.chicken
                    "meat" -> R.drawable.meat
                    "fish" -> R.drawable.fish
                    "pizza" -> R.drawable.pizza
                    "sandwich" -> R.drawable.sandwich
                    "soup" -> R.drawable.soup
                    else -> R.drawable.vegan
                }

                mainPhoto.setImageResource(id)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@FoodDetail, "No internet connection", Toast.LENGTH_LONG).show()
            }

        })

        val tableFood = database.getReference("Food")
        tableFood.child(ID.toString()).addValueEventListener(object: ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                val food = snapshot.getValue(Food::class.java)
                foodPrice.text = food?.price + "$"
                foodFullName.text = food?.full_text + "$"
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@FoodDetail, "No internet connection", Toast.LENGTH_LONG).show()
            }

        })

        val btnGoToCart = findViewById<Button>(R.id.btnGoToCart)
        btnGoToCart.setOnClickListener {
            startActivity(Intent(this, CardActivity::class.java))
        }

    }

    public fun btnAddToCart(view: View) {
        var cartList: MutableList<Cart?>? = JSONHelper.importFromJSON(this)?.toMutableList()

        if(cartList == null) {
            cartList = ArrayList()
            cartList.add(Cart(ID, 1))
        } else {
            var isFound = false
            for (el in cartList) {
                if (el?.categoryID == ID) {
                    el.amount++;
                    isFound = true
                }
            }
            if(!isFound)
                cartList.add(Cart(ID, 1))
        }

        JSONHelper.exportToJSON(this, cartList)
        Toast.makeText(this, "Added to cart", Toast.LENGTH_LONG).show()
    }
}