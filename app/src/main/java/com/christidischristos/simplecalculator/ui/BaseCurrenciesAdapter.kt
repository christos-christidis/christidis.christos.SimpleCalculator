package com.christidischristos.simplecalculator.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.christidischristos.simplecalculator.R
import com.christidischristos.simplecalculator.enums.Currency

class BaseCurrenciesAdapter(
    private var _items: List<Currency>, private val _clickListener: (Currency) -> Unit
) : RecyclerView.Adapter<BaseCurrenciesAdapter.ViewHolder>() {

    fun setItems(items: List<Currency>) {
        _items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val textView = LayoutInflater.from(parent.context)
            .inflate(R.layout.base_currencies_list_item, parent, false) as TextView
        return ViewHolder(textView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val view = holder.itemView as TextView
        view.text = _items[position].toString()
        view.setOnClickListener {
            _clickListener(_items[position])
        }
    }

    override fun getItemCount(): Int {
        return _items.size
    }

    class ViewHolder(view: TextView) : RecyclerView.ViewHolder(view)
}