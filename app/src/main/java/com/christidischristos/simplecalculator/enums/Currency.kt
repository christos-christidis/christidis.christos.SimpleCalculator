package com.christidischristos.simplecalculator.enums

import androidx.annotation.StringRes
import com.christidischristos.simplecalculator.R

enum class Currency {
    USD, EUR, JPY, GBP, AUD, CAD, CHF;

    @StringRes
    fun toTitleRes(): Int {
        return when (this) {
            USD -> R.string.usd
            EUR -> R.string.eur
            JPY -> R.string.jpy
            GBP -> R.string.gbp
            AUD -> R.string.aud
            CAD -> R.string.cad
            CHF -> R.string.chf
        }
    }

    companion object {
        val nonEuroCurrencies = values().joinToString(",")
            .replace("EUR,", "")
    }
}