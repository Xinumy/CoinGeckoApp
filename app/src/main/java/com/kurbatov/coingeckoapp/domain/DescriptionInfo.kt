package com.kurbatov.coingeckoapp.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DescriptionInfo(
    @SerializedName("en")
    @Expose
    val en: String?
)
