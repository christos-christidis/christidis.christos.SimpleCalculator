package com.christidischristos.simplecalculator.util

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.christidischristos.simplecalculator.enums.CalcState
import com.christidischristos.simplecalculator.enums.Currency
import com.christidischristos.simplecalculator.ui.BaseCurrenciesAdapter

@BindingAdapter("base_currency")
fun RecyclerView.setCurrencies(currency: Currency) {
    val currencies = Currency.getAllExcept(currency)
    (adapter as BaseCurrenciesAdapter).setItems(currencies)
}

@BindingAdapter("text_color")
fun TextView.setTextColor(state: CalcState) {
    val colorResId = if (state == CalcState.SHOWING_ERROR)
        android.R.color.holo_red_dark else android.R.color.black
    setTextColor(ContextCompat.getColor(context, colorResId))
}