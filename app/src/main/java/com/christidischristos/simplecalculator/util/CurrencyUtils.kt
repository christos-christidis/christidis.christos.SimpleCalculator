package com.christidischristos.simplecalculator.util

import com.christidischristos.simplecalculator.enums.Currency
import com.christidischristos.simplecalculator.network.HistoricalRatesResponse

// I go to all this trouble just to avoid paying for a plan! I can only query the rates of other
// currencies wrt EUR and I have to convert from any currency to any other currency w those...
object CurrencyUtils {

    private fun Map<Currency, Double>.getNonNullValue(currency: Currency): Double {
        return this[currency] ?: error("CurrencyUtils: This should never happen.")
    }

    fun getRate(
        baseCurrency: Currency, targetCurrency: Currency, response: HistoricalRatesResponse
    ): Double {
        requireNotNull(response.rates)

        val mapToRate = mapOf(
            Currency.USD to response.rates.USD, Currency.JPY to response.rates.JPY,
            Currency.GBP to response.rates.GBP, Currency.AUD to response.rates.AUD,
            Currency.CAD to response.rates.CAD, Currency.CHF to response.rates.CHF,
            Currency.EUR to 1.0
        )

        return if (baseCurrency == Currency.EUR) {
            mapToRate.getNonNullValue(targetCurrency)
        } else {
            1 / mapToRate.getNonNullValue(baseCurrency) * mapToRate.getNonNullValue(targetCurrency)
        }
    }
}
