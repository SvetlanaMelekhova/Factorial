package com.svetlana.learn.factorial

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigInteger
import kotlin.concurrent.thread
import kotlin.coroutines.suspendCoroutine

class MainViewModel : ViewModel() {

    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    fun calculate(value: String?) {
        _state.value = Progress
        if (value.isNullOrBlank()) {
            _state.value = Error
            return
        }
        viewModelScope.launch {
            val number = value.toLong()
            val result = factorial1(number)
            _state.value = Factorial(result)
        }
    }

    private suspend fun factorial1(number: Long): String{
        return withContext(Dispatchers.Default){
            var result = BigInteger.ONE
            for (i in 1..number){
                result = result.multiply(BigInteger.valueOf(i))
            }
            result.toString()
        }
    }

    private suspend fun factorial2(number: Long): String{
        return suspendCoroutine {
            thread {
                var result = BigInteger.ONE
                for (i in 1..number){
                    result = result.multiply(BigInteger.valueOf(i))
                }
                it.resumeWith(Result.success(result.toString()))
            }
        }
    }
}