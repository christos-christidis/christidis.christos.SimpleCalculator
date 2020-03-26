package com.christidischristos.simplecalculator.enums

import androidx.annotation.DrawableRes
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

    @DrawableRes
    fun getFlagResId(): Int {
        return when (this) {
            USD -> R.drawable.us_flag
            EUR -> R.drawable.eu_flag
            JPY -> R.drawable.japan_flag
            GBP -> R.drawable.uk_flag
            AUD -> R.drawable.australia_flag
            CAD -> R.drawable.canada_flag
            CHF -> R.drawable.swiss_flag
        }
    }

    companion object {
        val nonEuroCurrencies = values().joinToString(",")
            .replace("EUR,", "")

        fun getAllExcept(currency: Currency): List<Currency> {
            return values().toMutableList().apply {
                remove(currency)
            }
        }
    }
}