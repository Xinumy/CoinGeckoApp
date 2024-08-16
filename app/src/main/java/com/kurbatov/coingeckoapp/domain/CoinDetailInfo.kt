package com.kurbatov.coingeckoapp.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CoinDetailInfo(
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("name")
    @Expose
    val name: String?,
    @SerializedName("categories")
    @Expose
    val categories: List<String>?,
    @SerializedName("description")
    @Expose
    val description: DescriptionInfo?,
    @SerializedName("image")
    @Expose
    val image: ImageInfo?
) {

}
