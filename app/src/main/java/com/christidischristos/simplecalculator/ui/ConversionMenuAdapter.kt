package com.christidischristos.simplecalculator.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.christidischristos.simplecalculator.R
import com.christidischristos.simplecalculator.enums.Currency

class ConversionMenuAdapter(
    private val _items: List<Currency>,
    private val _clickListener: (Currency) -> Unit
) : RecyclerView.Adapter<ConversionMenuAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val linearLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.conversion_menu_list_item, parent, false)
        return ViewHolder(linearLayout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val view = holder.itemView as LinearLayout
        val imageView = view.findViewById<ImageView>(R.id.flag_image_view)
        val textView = view.findViewById<TextView>(R.id.target_currency_text)

        val currency = _items[position]

        imageView.setImageResource(currency.getFlagResId())
        textView.setText(currency.toTitleRes())

        view.setOnClickListener {
            _clickListener(currency)
        }
    }

    override fun getItemCount(): Int {
        return _items.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
