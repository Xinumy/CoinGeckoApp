package com.kurbatov.coingeckoapp.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "full_price_list")
data class CoinPriceInfo (
    @PrimaryKey
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("name")
    @Expose
    val name: String?,
    @SerializedName("symbol")
    @Expose
    val symbols: String?,
    @SerializedName("current_price")
    @Expose
    val price: Double?,
    @SerializedName("price_change_percentage_24h")
    @Expose
    val price_change_percentage_24h: String? = null,
    @SerializedName("image")
    @Expose
    val imageUrl: String?
){

}