package com.kurbatov.coingeckoapp.presentation

import CoinInfoAdapter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.kurbatov.coingeckoapp.R
import com.kurbatov.coingeckoapp.databinding.ActivityCoinPriceListBinding
import com.kurbatov.coingeckoapp.domain.CoinPriceInfo

class CoinPriceListActivity : AppCompatActivity() {

    private lateinit var viewModel: CoinPriceListViewModel
    private var binding: ActivityCoinPriceListBinding? = null

    private var pullToRefresh = false

    private val myLog = "MyLog"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinPriceListBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val coinInfoAdapter = CoinInfoAdapter(this)
        coinInfoAdapter.onCoinClickListener = object : CoinInfoAdapter.OnCoinClickListener {
            override fun onCoinClick(coinPriceInfo: CoinPriceInfo) {
                val intent = CoinDetailActivity.newIntent(
                    this@CoinPriceListActivity,
                    coinPriceInfo.id, coinPriceInfo.name.toString()
                )
                startActivity(intent)
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

        viewModel.getIsLoading().observe(this) { isLoading ->
            binding?.progressBarLoadingCoinPriceList?.visibility =
                if (isLoading == true) View.VISIBLE else View.GONE
            if (pullToRefresh && isLoading == false) {
                binding?.swipeRefreshLayout?.isRefreshing = false
                pullToRefresh = false
            }
        }

        viewModel.getIsLoadingFail().observe(this) { isLoadingFail ->
            if (isLoadingFail) {
                if (pullToRefresh) {
                    binding?.swipeRefreshLayout?.isRefreshing = false
                    pullToRefresh = false
                    binding?.root?.let {
                        Snackbar.make(
                            it,
                            "Произошла ошибка при загрузке",
                            Snackbar.LENGTH_SHORT
                        )
                            .setBackgroundTint(ContextCompat.getColor(this, R.color.snackbar_red))
                            .setTextColor(ContextCompat.getColor(this, R.color.white))
                            .show()
                    }
                    binding?.recyclerViewCoinPriceList?.visibility = View.VISIBLE
                    binding?.linearLayoutConnectionFailed?.visibility = View.GONE
                } else {
                    binding?.recyclerViewCoinPriceList?.visibility = View.GONE
                    binding?.linearLayoutConnectionFailed?.visibility = View.VISIBLE
                }
            } else {
                binding?.recyclerViewCoinPriceList?.visibility = View.VISIBLE
                binding?.linearLayoutConnectionFailed?.visibility = View.GONE
            }
        }

        binding?.buttonReconnect?.setOnClickListener {
            viewModel.loadData()
        }

        binding?.swipeRefreshLayout?.setOnRefreshListener {
            pullToRefresh = true
            viewModel.loadData()
        }

        binding!!.chipCurrencyGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chipUSD -> currentSelectedCurrency = "usd"
                R.id.chipRUB -> currentSelectedCurrency = "rub"
            }
            viewModel.loadData()
        }
    }

    companion object {
        var currentSelectedCurrency = "usd"
    }
}
