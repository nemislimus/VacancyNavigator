package ru.practicum.android.diploma.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.db.XxxDataBase
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.data.network.api.HhSearchApi
import ru.practicum.android.diploma.data.network.api.NetworkClient
import ru.practicum.android.diploma.data.network.mapper.NetworkMapper
import java.net.URLEncoder
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

val dataModule = module {

    single<XxxDataBase> {
        Room.databaseBuilder(androidContext(), XxxDataBase::class.java, "xxx-team.db").build()
    }
    single<ConnectivityManager> {
        androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    single<NetworkMapper> {
        NetworkMapper()
    }
    single<NetworkClient> {
        RetrofitNetworkClient(
            hhSearchApi = get(),
            connectivityManager = get(),
            mapper = get()
        )
    }
    single<HhSearchApi> {

        val interceptor = Interceptor { chain ->

            val token = BuildConfig.HH_ACCESS_TOKEN
            val appname = "Навигатор Вакансий XXX"

            val appNameUrl = URLEncoder.encode(appname, StandardCharsets.UTF_8.toString())
            val mail = "amdoit.com@gmail.com"

            val originalRequest = chain.request()
            val builder = originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .header("HH-User-Agent", "$appNameUrl ($mail)")
            val newRequest = builder.build()
            chain.proceed(newRequest)

        }

        val okHttpClient = OkHttpClient()
            .newBuilder()
            .addInterceptor(interceptor)
            .build()

        Retrofit.Builder()
            .baseUrl("https://api.hh.ru/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(HhSearchApi::class.java)
    }

}
