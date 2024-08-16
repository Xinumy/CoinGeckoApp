package com.kurbatov.coingeckoapp.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cryptoapp.api.ApiFactory
import com.kurbatov.coingeckoapp.data.AppDatabase
import com.kurbatov.coingeckoapp.domain.CoinDetailInfo
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class CoinDetailViewModel (application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val compositeDisposable = CompositeDisposable()

    private val coinDetailInfo = MutableLiveData<CoinDetailInfo>()

    fun getCoinDetailInfo(): LiveData<CoinDetailInfo> {
        return coinDetailInfo
    }

    private val isLoading = MutableLiveData(false)

    fun getIsLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun loadDetailData(currentId : String) {
        val disposable = ApiFactory.apiService.getCoinDetailInfo(id = currentId)
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                isLoading.postValue(true)
            }
            .doAfterTerminate {
                isLoading.postValue(false)
            }
            .subscribe({
                coinDetailInfo.postValue(it)
                //db.coinDetailInfoDao().insertDetailInfo(it)

                Log.d("TEST_OF_LOADING_DATA", it.toString())
            }, {
                Log.d("TEST_OF_LOADING_DATA", it.toString())
            })
        compositeDisposable.add(disposable)
    }
}