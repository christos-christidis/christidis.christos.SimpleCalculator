package com.christidischristos.simplecalculator.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "http://data.fixer.io/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()


interface ConvertService {
    @GET("{date}")
    suspend fun getExchangeRate(
        @Path("date") yesterday: String,
        @Query("access_key") apiKey: String,
        @Query("base") baseCurrency: String,
        @Query("symbols") targetCurrencies: String
    ): HistoricalRatesResponse
}

object FixerApi {
    val convertService: ConvertService by lazy {
        retrofit.create(ConvertService::class.java)
    }
}

