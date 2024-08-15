import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.api.ApiService
import com.kurbatov.coingeckoapp.R
import com.kurbatov.coingeckoapp.databinding.ItemCoinInfoBinding
import com.kurbatov.coingeckoapp.domain.CoinPriceInfo
import com.kurbatov.coingeckoapp.presentation.CoinPriceListActivity.Companion.currentSelectedCurrency
import com.squareup.picasso.Picasso

class CoinInfoAdapter(private val context: Context) :
    RecyclerView.Adapter<CoinInfoAdapter.CoinInfoViewHolder>() {

    var coinInfoList: List<CoinPriceInfo> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onCoinClickListener: OnCoinClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinInfoViewHolder {
        val binding = ItemCoinInfoBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return CoinInfoViewHolder(binding)
    }

    override fun getItemCount() = coinInfoList.size

    override fun onBindViewHolder(holder: CoinInfoViewHolder, position: Int) {
        val coin = coinInfoList[position]
        with(holder) {
            with(coin) {
                textViewName.text = name.toString()
                tvSymbols.text = symbols.toString().uppercase()
                var currencySymbol = "$ "
                if(currentSelectedCurrency == "rub" ){
                    currencySymbol = "₽ "
                }
                tvPrice.text = buildString {
                    append(currencySymbol)
                    append(price.toString())
                }
                val number = price_change_percentage_24h?.toDoubleOrNull()
                val roundedPercentage = String.format("%.2f", number)
                val signedNumber = if (number!! >= 0) "+$roundedPercentage%" else "$roundedPercentage%"
                if(price_change_percentage_24h?.toFloat()!! < 0){
                    tvPercentage.setTextColor(ContextCompat.getColor(context, R.color.red_light))
                }
                else{
                    tvPercentage.setTextColor(ContextCompat.getColor(context, R.color.green_light))
                }
                tvPercentage.text = signedNumber
                Picasso.get().load(imageUrl).into(ivLogoCoin)
                itemView.setOnClickListener {
                    onCoinClickListener?.onCoinClick(this)
                }
            }
        }
    }

    inner class CoinInfoViewHolder(binding: ItemCoinInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val textViewName = binding.textViewName
        val ivLogoCoin = binding.imageViewLogoCoin
        val tvSymbols = binding.textViewSymbols
        val tvPrice = binding.textViewPrice
        val tvPercentage = binding.textViewPercent
    }

    interface OnCoinClickListener {
        fun onCoinClick(coinPriceInfo: CoinPriceInfo)
    }
}