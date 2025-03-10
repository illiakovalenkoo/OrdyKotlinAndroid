package com.feodal.ordy

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.feodal.ordy.SignIn.Companion.setDefaults
import com.feodal.ordy.helpers.CartListAdapter
import com.feodal.ordy.helpers.JSONHelper
import com.feodal.ordy.models.Cart
import com.feodal.ordy.models.Order
import com.feodal.ordy.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date

class CardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_card)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val listView = findViewById<ListView>(R.id.shopping_cart)
        val cartList = JSONHelper.importFromJSON(this)

        if(cartList == null) {
            Toast.makeText(this, "No data in bin", Toast.LENGTH_LONG).show()
        } else {
            val arrayAdapter = CartListAdapter(this, R.layout.cart_item, cartList)
            listView.adapter = arrayAdapter
            Toast.makeText(this, "Bin has been restored", Toast.LENGTH_LONG).show()
        }

        val database = FirebaseDatabase.getInstance("https://ordy-f431d-default-rtdb.europe-west1.firebasedatabase.app/")
        val table = database.getReference("Order")

        val btnMakeOrder = findViewById<Button>(R.id.buttonMakeOrder)
        btnMakeOrder.setOnClickListener {
            table.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val json = JSONHelper.importFromJSON(this@CardActivity)

                    if(json == null) {
                        Toast.makeText(this@CardActivity, "Bin is empty", Toast.LENGTH_LONG).show()
                        return
                    }
                    val userPhone = SignIn.getDefaults("UserPhone",this@CardActivity)
                    val userName = SignIn.getDefaults("UserName",this@CardActivity)
                    val order = Order(json.toString(), userPhone, userName)

                    val dateFormat = SimpleDateFormat("dd-MM HH:mm")
                    val currentDate = dateFormat.format(Date())

                    table.child(currentDate).setValue(order).addOnSuccessListener {

                        val dataList: List<Cart> = ArrayList()
                        JSONHelper.exportToJSON(this@CardActivity, dataList)
                        val arrayAdapter = CartListAdapter(this@CardActivity, R.layout.cart_item, dataList)
                        listView.adapter = arrayAdapter
                    }

                    Toast.makeText(this@CardActivity, "Order was completed", Toast.LENGTH_LONG).show()

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@CardActivity, "No internet connection", Toast.LENGTH_LONG).show()
                }

            })
        }
    }
}