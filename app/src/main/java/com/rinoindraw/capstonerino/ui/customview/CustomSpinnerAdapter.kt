package com.rinoindraw.capstonerino.ui.customview

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.rinoindraw.capstonerino.R

@Suppress("DEPRECATION")
class CustomSpinnerAdapter(context: Context, private val itemList: List<String>) :
    ArrayAdapter<String>(context, R.layout.spinner_item, itemList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getCustomView(position: Int, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.spinner_item, parent, false)
        val textView = view.findViewById<TextView>(android.R.id.text1)

        textView.text = itemList[position]
        textView.setTextColor(context.resources.getColor(R.color.blue_green))
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)

        val drawable = context.resources.getDrawable(R.drawable.baseline_arrow_drop_down_24)
        textView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)

        return view
    }
}