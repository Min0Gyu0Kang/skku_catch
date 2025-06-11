package com.example.pa2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class Activity3Adapter(
    private val context: Context,
    private val menuList: List<MenuItem>
) : BaseAdapter() {
    override fun getCount() = menuList.size
    override fun getItem(position: Int) = menuList[position]
    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.a3_reservations, parent, false)
        val menuItem = menuList[position]

        val thumbnail = view.findViewById<ImageView>(R.id.a3_thumbnail)
        val name = view.findViewById<TextView>(R.id.a3_menu_name)
        val price = view.findViewById<TextView>(R.id.price)

        val resId = context.resources.getIdentifier(menuItem.image, "drawable", context.packageName)
        thumbnail.setImageResource(if (resId != 0) resId else R.drawable.ic_launcher_background)
        name.text = menuItem.name
        price.text = "$${menuItem.price}"

        return view
    }
}