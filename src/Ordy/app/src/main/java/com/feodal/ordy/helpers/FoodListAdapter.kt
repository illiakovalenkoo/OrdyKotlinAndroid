package com.feodal.ordy.helpers

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.feodal.ordy.FoodDetail
import com.feodal.ordy.R
import com.feodal.ordy.models.Category

class FoodListAdapter(
    private val mContext: Context,
    private val mResource: Int,
    private val categories: List<Category>) : ArrayAdapter<Category>(mContext, mResource, categories) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val mainView = LayoutInflater.from(mContext).inflate(mResource, parent, false)

        val category = categories[position]

        val foodName = mainView.findViewById<TextView>(R.id.mainName)
        val foodImage = mainView.findViewById<ImageView>(R.id.mainPhoto)

        foodName.text = category.name

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

        foodImage.setImageResource(id)

        foodImage.setOnClickListener {
            FoodDetail.ID = position + 1
            mContext.startActivity(Intent(mContext, FoodDetail::class.java))
        }

        return mainView
    }
}