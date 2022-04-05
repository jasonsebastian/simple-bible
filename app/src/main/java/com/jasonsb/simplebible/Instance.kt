package com.jasonsb.simplebible

import com.jasonsb.simplebible.esv.EsvService
import okhttp3.OkHttpClient

class Instance {
    companion object {
        private const val API_KEY = "aff10f58e446b794c273bc3eb27426ca59831b43"
        private var esvApi: EsvService? = null

        val okHttpClient: OkHttpClient by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            OkHttpClient().newBuilder()
                .addInterceptor { chain ->
                    val newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Token $API_KEY")
                        .build()
                    chain.proceed(newRequest)
                }.build()
        }

        fun getEsvApi(): EsvService = esvApi ?: synchronized(this) {
            esvApi ?: EsvService.create().also { esvApi = it }
        }
    }
}