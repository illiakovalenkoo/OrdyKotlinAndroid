package com.feodal.ordy

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.feodal.ordy.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnSignUp = findViewById<Button>(R.id.buttonSignUp)
        val editName = findViewById<EditText>(R.id.editTextName)
        val editPhone = findViewById<EditText>(R.id.editTextPhone)
        val editPass = findViewById<EditText>(R.id.editTextPassword)

        val database = FirebaseDatabase.getInstance("https://ordy-f431d-default-rtdb.europe-west1.firebasedatabase.app/")
        val table = database.getReference("User")

        btnSignUp.setOnClickListener {
            table.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.child(editPhone.text.toString()).exists()) {
                        Toast.makeText(this@SignUp, "Such user already exists", Toast.LENGTH_LONG).show()
                    } else {
                        val user = User(editName.text.toString(), editPass.text.toString())
                        table.child(editPhone.text.toString()).setValue(user)
                        editName.setText("")
                        editPhone.setText("")
                        editPass.setText("")
                        Toast.makeText(this@SignUp, "Successful registration", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@SignUp, "No internet connection", Toast.LENGTH_LONG).show()
                }

            })
        }
    }
}