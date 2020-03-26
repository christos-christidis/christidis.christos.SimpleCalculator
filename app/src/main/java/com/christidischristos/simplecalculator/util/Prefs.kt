package com.christidischristos.simplecalculator.util

import android.content.Context
import androidx.preference.PreferenceManager
import com.christidischristos.simplecalculator.R
import com.christidischristos.simplecalculator.network.ExchangeRates
import com.squareup.moshi.Moshi

object Prefs {
    private const val LAST_FETCH_TIME_KEY = "last_fetch_time_key"
    private const val CACHED_EXCHANGE_RATES_KEY = "cached_exchange_rates_key"

    fun getLastFetchTime(context: Context?): Long {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getLong(LAST_FETCH_TIME_KEY, 0L)
    }

    fun storeFetchTime(context: Context?, time: Long) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit().putLong(LAST_FETCH_TIME_KEY, time).apply()
    }

    fun getMaximumCacheTime(context: Context?): Long {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(context!!.getString(R.string.check_for_new_results_pref_key), "60")!!.toLong()
    }

    fun getCachedExchangeRates(context: Context?): ExchangeRates {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val str = prefs.getString(CACHED_EXCHANGE_RATES_KEY, "")
        val moshi = Moshi.Builder().build()
        return moshi.adapter(ExchangeRates::class.java).fromJson(str!!)!!
    }

    fun storeExchangeRates(context: Context?, exchangeRates: ExchangeRates) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val moshi = Moshi.Builder().build()
        val str = moshi.adapter(ExchangeRates::class.java).toJson(exchangeRates)
        prefs.edit().putString(CACHED_EXCHANGE_RATES_KEY, str).apply()
    }
}