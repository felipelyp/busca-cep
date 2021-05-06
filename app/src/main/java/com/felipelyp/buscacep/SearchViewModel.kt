package com.felipelyp.buscacep

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.felipelyp.buscacep.model.Cep
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class SearchViewModel : ViewModel() {

    val result by lazy { MutableLiveData<Result<Cep>>() }

    fun search(cep: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    val url = "https://viacep.com.br/ws/$cep/json/"

                    (URL(url).openConnection() as? HttpURLConnection)?.run {
                        requestMethod = "GET"

                        val response = parseResponse(inputStream)
                        Gson().fromJson(response, Cep::class.java)
                    }
                }

                result.value = Result.success(response!!)
            } catch (e: Exception) {
                result.value = Result.failure(Throwable("Sem conexão"))
            }
        }
    }

    private fun parseResponse(inputStream: InputStream): String {
        return inputStream.reader().use { reader -> reader.readText() }
    }
}