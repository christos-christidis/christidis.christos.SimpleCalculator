package com.christidischristos.simplecalculator.util

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.christidischristos.simplecalculator.enums.Currency
import com.christidischristos.simplecalculator.ui.BaseCurrenciesAdapter

@BindingAdapter("base_currency")
fun RecyclerView.setCurrencies(currency: Currency) {
    val currencies = Currency.getAllExcept(currency)
    (adapter as BaseCurrenciesAdapter).setItems(currencies)
}