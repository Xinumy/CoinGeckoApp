package com.kurbatov.coingeckoapp.presentation

import CoinInfoAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kurbatov.coingeckoapp.R
import com.kurbatov.coingeckoapp.databinding.ActivityCoinPriceListBinding
import com.kurbatov.coingeckoapp.domain.CoinPriceInfo

class CoinPriceListActivity : AppCompatActivity() {

    private lateinit var viewModel: CoinPriceListViewModel
    private var binding: ActivityCoinPriceListBinding? = null

    private val myLog = "MyLog"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinPriceListBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val coinInfoAdapter = CoinInfoAdapter(this)
        coinInfoAdapter.onCoinClickListener = object : CoinInfoAdapter.OnCoinClickListener {
            override fun onCoinClick(coinPriceInfo: CoinPriceInfo) {

            }
        }

        with(binding!!.recyclerViewCoinPriceList) {
            adapter = coinInfoAdapter
        }


        viewModel = ViewModelProvider(this)[CoinPriceListViewModel::class.java]
        viewModel.priceList.observe(this, Observer {
            coinInfoAdapter.coinInfoList = it
            Log.d(myLog, "Success in activity: $it")
        })

        binding!!.chipCurrencyGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chipUSD -> currentSelectedCurrency = "usd"
                R.id.chipRUB -> currentSelectedCurrency = "rub"
            }
            viewModel.setCurrencySymbol(currentSelectedCurrency)
        }

    }

    companion object {
        var currentSelectedCurrency = "usd"
    }
}
