package com.christidischristos.simplecalculator.util

import com.christidischristos.simplecalculator.enums.Currency
import com.christidischristos.simplecalculator.network.HistoricalRatesResponse

// The free plan only accepts queries w base_currency=EUR. Therefore, in order to be able to convert
// from ANY currency to any other, I have to first convert to euro and THEN to the target_currency
object CurrencyUtils {

    private fun Map<Currency, Double>.getNonNullValue(currency: Currency): Double {
        return this[currency] ?: error("CurrencyUtils: This should never happen.")
    }

    fun computeExchangeRate(
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
