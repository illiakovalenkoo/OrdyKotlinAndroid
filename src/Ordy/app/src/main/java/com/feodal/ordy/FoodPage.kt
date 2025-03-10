package com.feodal.ordy

import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.feodal.ordy.helpers.FoodListAdapter
import com.feodal.ordy.models.Category
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FoodPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_food_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val database = FirebaseDatabase.getInstance("https://ordy-f431d-default-rtdb.europe-west1.firebasedatabase.app/")
        val table = database.getReference("Category")

        val listView = findViewById<ListView>(R.id.list_of_food)

        table.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val allFood = ArrayList<Category>()
                for(obj in snapshot.children) {
                    val category = obj.getValue(Category::class.java)
                    if(category != null)
                        allFood.add(category)
                }

                val arrayAdapter = FoodListAdapter(this@FoodPage, R.layout.food_item_in_list, allFood)
                listView.adapter = arrayAdapter

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@FoodPage, "No internet connection", Toast.LENGTH_LONG).show()
            }

        })
    }
}