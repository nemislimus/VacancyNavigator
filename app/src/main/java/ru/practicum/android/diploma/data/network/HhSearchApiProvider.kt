package ru.practicum.android.diploma.data.network

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.CACHE_SIZE_BYTES
import ru.practicum.android.diploma.data.network.api.HhSearchApi
import ru.practicum.android.diploma.domain.repository.NetworkConnectionCheckerRepository
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class HhSearchApiProvider(
    scope: CoroutineScope,
    private val checker: NetworkConnectionCheckerRepository,
    private val context: Context
) {
    private var api: HhSearchApi? = null

    init {
        scope.launch {
            checker.onStateChange().collect { isConnected ->
                if (!isConnected) {
                    api = null
                }
            }
        }
    }

    /** Сетевой клиент
     * @author Ячменев И.
     * описание проблемы которую решает код ниже можно прочитать тут
     * https://stackoverflow.com/questions/37885391/okhttp-sslprotocolexception-ssl-handshake-terminated
     * https://stackoverflow.com/questions/36455656/java-net-sockettimeoutexception-timeout
     * */
    fun getApi(): HhSearchApi {
        api?.let { actualApi ->
            return actualApi
        }
        val interceptor = Interceptor { chain ->

            val token = BuildConfig.HH_ACCESS_TOKEN
            val appname = "Навигатор Вакансий XXX"

            val appNameUrl = URLEncoder.encode(appname, StandardCharsets.UTF_8.toString())
            val mail = "amdoit.com@gmail.com"

            val originalRequest = chain.request()
            val builder = originalRequest
                .newBuilder()
                .header("Authorization", "Bearer $token")
                .header("HH-User-Agent", "$appNameUrl ($mail)")
            val newRequest = builder.build()
            chain.proceed(newRequest)
        }

        val okHttpClient = OkHttpClient()
            .newBuilder()
            .cache(Cache(context.cacheDir, CACHE_SIZE_BYTES))
            .addInterceptor(interceptor)
            .build()

        val client = Retrofit
            .Builder()
            .baseUrl("https://api.hh.ru/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(HhSearchApi::class.java)

        if (checker.isConnected()) {
            api = client
        }

        return client
    }
}
