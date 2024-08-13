import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CoinInfo (
    @SerializedName("name")
    @Expose
    val name: String? = null,

    @SerializedName("symbol")
    @Expose
    val symbol: String? = null,

    @SerializedName("current_price")
    @Expose
    val currentPrice: String? = null,

    @SerializedName("price_change_percentage_24h")
    @Expose
    val price_change_percentage_24h: String? = null,

    @SerializedName("image")
    @Expose
    val image: String? = null,

)