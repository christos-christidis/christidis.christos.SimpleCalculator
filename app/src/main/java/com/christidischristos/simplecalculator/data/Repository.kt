package com.christidischristos.simplecalculator.data

import android.app.Application
import com.christidischristos.simplecalculator.BuildConfig
import com.christidischristos.simplecalculator.enums.Currency
import com.christidischristos.simplecalculator.network.FixerApi
import com.christidischristos.simplecalculator.network.HistoricalRatesResponse
import com.christidischristos.simplecalculator.util.Prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class Repository(private val _app: Application) {
    fun fetchExchangeRates(
        onSuccess: (response: HistoricalRatesResponse) -> Unit,
        onError: (errorCode: Int) -> Unit,
        onException: () -> Unit
    ) {
        if (cachedDataIsValid()) {
            val exchangeRates = Prefs.getCachedExchangeRates(_app)
            onSuccess(HistoricalRatesResponse(true, exchangeRates, null))
            return
        }

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = FixerApi.convertService.getExchangeRates(
                    getYesterdayDateString(), BuildConfig.FIXER_IO_KEY,
                    "EUR", Currency.nonEuroCurrencies
                )
                if (response.success) {
                    onSuccess(response)
                    Prefs.storeFetchTime(_app, Date().time)
                    Prefs.storeExchangeRates(_app, response.rates!!)
                } else {
                    onError(response.error!!.code)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onException()
            }
        }
    }

    private fun getYesterdayDateString(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -1)
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(calendar.time)
    }

    private fun cachedDataIsValid(): Boolean {
        val now = Date().time   // returns milliseconds since 1970
        val lastFetchTime = Prefs.getLastFetchTime(_app)
        val maximumCacheTime = Prefs.getMaximumCacheTime(_app)

        return now - lastFetchTime <= TimeUnit.MINUTES.toMillis(maximumCacheTime)
    }
}
