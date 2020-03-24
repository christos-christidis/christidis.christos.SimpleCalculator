package com.christidischristos.simplecalculator.network

data class ExchangeRates(
    val USD: Double?, val EUR: Double?, val JPY: Double?, val GBP: Double?,
    val AUD: Double?, val CAD: Double?, val CHF: Double?
)

data class FixerApiError(val code: Int, val info: String)

data class HistoricalRatesResponse(
    val success: Boolean,
    val rates: ExchangeRates?,
    val error: FixerApiError?
)