package com.christidischristos.simplecalculator.data

import com.christidischristos.simplecalculator.BuildConfig
import com.christidischristos.simplecalculator.enums.Currency
import com.christidischristos.simplecalculator.network.FixerApi
import com.christidischristos.simplecalculator.network.HistoricalRatesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class Repository {
    fun fetchExchangeRates(
        onSuccess: (response: HistoricalRatesResponse) -> Unit,
        onError: (errorCode: Int) -> Unit,
        onException: () -> Unit
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = FixerApi.convertService.getExchangeRate(
                    getYesterdayDateString(), BuildConfig.FIXER_IO_KEY,
                    "EUR", Currency.nonEuroCurrencies
                )
                if (response.success) {
                    onSuccess(response)
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
}
