package com.feodal.ordy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.preference.PreferenceManager
import com.feodal.ordy.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SignIn : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnSignIn = findViewById<Button>(R.id.buttonSignIn)
        val editPhone = findViewById<EditText>(R.id.editTextPhone)
        val editPass = findViewById<EditText>(R.id.editTextPassword)

        val database = FirebaseDatabase.getInstance("https://ordy-f431d-default-rtdb.europe-west1.firebasedatabase.app/")
        val table = database.getReference("User")

        btnSignIn.setOnClickListener {
            table.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.child(editPhone.text.toString()).exists()) {
                        val user: User? = snapshot.child(editPhone.text.toString()).getValue(User::class.java)
                        if(editPass.text.toString().equals(user?.pass)) {
                            setDefaults("UserPhone", editPhone.text.toString(), this@SignIn)
                            setDefaults("UserName", user?.name.toString(), this@SignIn)

                            val intent = Intent(this@SignIn, FoodPage::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@SignIn, "Password is incorrect", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this@SignIn, "No user with such number", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@SignIn, "No internet connection", Toast.LENGTH_LONG).show()
                }

            })
        }
    }

    companion object {
        fun setDefaults(key: String, value: String?, context: Context) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = prefs.edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun getDefaults(key: String, context: Context): String? {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return prefs.getString(key, null)
        }
    }

}