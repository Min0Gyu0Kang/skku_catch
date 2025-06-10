package com.example.pa2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.res.ResourcesCompat

class RestaurantAdapter(
    private val context: Context,
    private val restaurants: List<Restaurant>
) : BaseAdapter() {
    override fun getCount() = restaurants.size
    override fun getItem(position: Int) = restaurants[position]
    override fun getItemId(position: Int) = restaurants[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.a1_reservations, parent, false)
        val restaurant = restaurants[position]

        val name = view.findViewById<TextView>(R.id.restaurant_name)
        val type = view.findViewById<TextView>(R.id.restaurant_type)
        val location = view.findViewById<TextView>(R.id.restaurant_location)
        val thumbnail = view.findViewById<ImageView>(R.id.thumbnail)

        name.text = restaurant.name
        type.text = restaurant.type
        location.text = restaurant.location

        // 썸네일 리소스 이름으로 이미지 설정
        val resId = context.resources.getIdentifier(restaurant.image, "drawable", context.packageName)
        if (resId != 0) {
            thumbnail.setImageResource(resId)
        } else {
            thumbnail.setImageResource(R.drawable.ic_launcher_background)
        }

        return view
    }
}