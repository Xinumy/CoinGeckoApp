package com.kurbatov.coingeckoapp.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cryptoapp.api.ApiFactory
import com.kurbatov.coingeckoapp.data.AppDatabase
import com.kurbatov.coingeckoapp.presentation.CoinPriceListActivity.Companion.currentSelectedCurrency
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class CoinPriceListViewModel (application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val compositeDisposable = CompositeDisposable()

    val priceList = db.coinPriceInfoDao().getPriceList()

    private val isLoading = MutableLiveData(false)

    fun getIsLoading(): LiveData<Boolean> {
        return isLoading
    }

    private val isLoadingFail = MutableLiveData(false)

    fun getIsLoadingFail(): LiveData<Boolean> {
        return isLoadingFail
    }

    init{
        loadData()
    }

    fun loadData() {
        val disposable = ApiFactory.apiService.getCoinsInfo(vsCurrency = currentSelectedCurrency)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                isLoading.postValue(true)
            }
            .doAfterTerminate {
                isLoading.postValue(false)
            }
            .subscribe({
                db.coinPriceInfoDao().insertPriceList(it)
                isLoadingFail.postValue(false)
                Log.d("TEST_OF_LOADING_DATA", "Success: $it")
            }, {
                db.coinPriceInfoDao().clearPriceList()
                isLoadingFail.postValue(true)
                Log.d("TEST_OF_LOADING_DATA", it.toString())
            })
        compositeDisposable.add(disposable)
    }
}