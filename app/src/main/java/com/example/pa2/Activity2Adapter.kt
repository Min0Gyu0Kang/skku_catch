package com.example.pa2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class Activity2Adapter(
    private val context: Context,
    private val restaurantInfos: List<RestaurantInfo>,
    private val openingHours: List<Pair<String, String>>
) : BaseAdapter() {
    override fun getCount() = restaurantInfos.size
    override fun getItem(position: Int) = restaurantInfos[position]
    override fun getItemId(position: Int) = restaurantInfos[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.a2_reservations, parent, false)
        val restaurant = restaurantInfos[position]
        val openClose = openingHours[position]

        val thumbnail = view.findViewById<ImageView>(R.id.a2_thumbnail)
        val name = view.findViewById<TextView>(R.id.a2_restaurant_name)
        val openCloseText = view.findViewById<TextView>(R.id.a2_open_close)
        val locationRating = view.findViewById<TextView>(R.id.a2_location_rating)

        val resId =
            context.resources.getIdentifier(restaurant.image, "drawable", context.packageName)
        if (resId != 0) {
            thumbnail.setImageResource(resId)
        } else {
            thumbnail.setImageResource(R.drawable.ic_launcher_background)
        }

        name.text = restaurant.name
        openCloseText.text = "${openClose.first} - ${openClose.second}"
        locationRating.text = "${restaurant.location} / ${restaurant.rating}"

        return view
    }
}