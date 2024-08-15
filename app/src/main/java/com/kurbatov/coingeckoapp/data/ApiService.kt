package com.example.cryptoapp.api

import com.kurbatov.coingeckoapp.domain.CoinPriceInfo
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/v3/coins/markets")

    fun getCoinsInfo(
        @Query(QUERY_PARAM_API_KEY) apiKey: String = "CG-pw2PDxEwTAXLXewoFJQercR3",
        @Query(QUERY_PARAM_LIMIT) limit: Int = 20,
        @Query(QUERY_PARAM_CURRENCY) vsCurrency: String = CURRENCY
    ): Single<List<CoinPriceInfo>>

    companion object {
        private const val QUERY_PARAM_API_KEY = "x_cg_demo_api_key"
        private const val QUERY_PARAM_LIMIT = "per_page"
        private const val QUERY_PARAM_CURRENCY = "vs_currency"
        private const val CURRENCY = "usd"
    }
}