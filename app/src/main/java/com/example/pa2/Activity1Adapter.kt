package com.example.pa2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

class Activity1Adapter(
    private val context: Context,
    private val reservations: List<UserInfo>
) : BaseAdapter() {
    override fun getCount() = reservations.size
    override fun getItem(position: Int) = reservations[position]
    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.a1_reservations, parent, false)
        val reservation = reservations[position]

        val name = view.findViewById<TextView>(R.id.restaurant_name)
        val time = view.findViewById<TextView>(R.id.a1_time)
        val date = view.findViewById<TextView>(R.id.a1_date)
        val people = view.findViewById<TextView>(R.id.a1_people)
        val thumbnail = view.findViewById<ImageView>(R.id.thumbnail)

        name.text = reservation.restaurantName
        time.text = reservation.time
        date.text = reservation.date
        people.text = "People: ${reservation.numberOfPeople}"

        val resId = context.resources.getIdentifier(
            reservation.restaurantImage,
            "drawable",
            context.packageName
        )
        if (resId != 0) {
            thumbnail.setImageResource(resId)
        } else {
            thumbnail.setImageResource(R.drawable.ic_launcher_background)
        }

        return view
    }
}