package com.kurbatov.coingeckoapp.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kurbatov.coingeckoapp.R
import com.kurbatov.coingeckoapp.databinding.ActivityCoinDetailBinding
import com.kurbatov.coingeckoapp.databinding.ActivityCoinPriceListBinding
import com.kurbatov.coingeckoapp.domain.CoinPriceInfo
import com.kurbatov.coingeckoapp.presentation.CoinPriceListActivity.Companion.currentSelectedCurrency
import com.squareup.picasso.Picasso

class CoinDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: CoinDetailViewModel
    private var binding: ActivityCoinDetailBinding? = null

    private val myLog = "MyLog"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        if (!intent.hasExtra(EXTRA_ID) && intent.hasExtra(EXTRA_NAME)) {
            finish()
            return
        }

        binding?.toolbarTitle?.text = intent.getStringExtra(EXTRA_NAME)
        val currentId = intent.getStringExtra(EXTRA_ID) ?: ""
        viewModel = ViewModelProvider(this)[CoinDetailViewModel::class.java]
        viewModel.loadDetailData(currentId)
        viewModel.getIsLoading().observe(this) { isLoading ->
            binding?.progressBarLoadingDetail?.visibility =
                if (isLoading == true) View.VISIBLE else View.GONE
        }
        binding?.toolbarButton?.setOnClickListener {
            finish()
        }

        viewModel.getIsLoadingFail().observe(this) { isLoadingFail ->
            binding?.linearLayoutDetailConnectionFailed?.visibility =
                if (isLoadingFail == true) View.VISIBLE else View.GONE
            binding?.textViewDescriptionTitle?.visibility =
                if (isLoadingFail == false) View.VISIBLE else View.GONE
            binding?.textViewCategoriesTitle?.visibility =
                if (isLoadingFail == false) View.VISIBLE else View.GONE
        }

        binding?.buttonReconnectDetail?.setOnClickListener {
            viewModel.loadDetailData(currentId)
        }

        viewModel.getCoinDetailInfo().observe(this, Observer {

            binding?.textViewDescription?.text = it.description?.en?.replace(Regex("<.*?>"), "")
            binding?.textViewCategories?.text = it.categories.toString().removeSurrounding("[", "]")
            Picasso.get().load(it.image?.large).into(binding?.imageViewDetailLogo)
            Log.d(myLog, "Success in activity: $it")
        })

    }

    companion object {
        private var EXTRA_ID = "ID"
        private var EXTRA_NAME = ""

        fun newIntent(context: Context, id: String, name: String): Intent {
            val intent = Intent(context, CoinDetailActivity::class.java)
            intent.putExtra(EXTRA_ID, id)
            intent.putExtra(EXTRA_NAME, name)
            return intent
        }

    }
}