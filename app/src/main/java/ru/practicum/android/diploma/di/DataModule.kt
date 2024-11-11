package ru.practicum.android.diploma.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import android.content.Context
import android.net.ConnectivityManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.data.db.XxxDataBase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.network.HhSearchApi
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient

val dataModule = module {

    single<XxxDataBase> {
        Room.databaseBuilder(androidContext(), XxxDataBase::class.java, "xxx-team.db").build()
    }
    single<ConnectivityManager> { androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }
    single<NetworkClient> {
        RetrofitNetworkClient(hhSearchApi = get(), connectivityManager = get())
    }
    single<HhSearchApi> {

        val interceptor = Interceptor { chain ->

            val token = BuildConfig.HH_ACCESS_TOKEN
            val appname = ""
            val mail = ""

            val originalRequest = chain.request()
            val builder = originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .header("HH-User-Agent", "$appname ($mail)")
            val newRequest = builder.build()
            chain.proceed(newRequest)
        }

        val okHttpClient = OkHttpClient().newBuilder().addInterceptor(interceptor).build()

        Retrofit.Builder()
            .baseUrl("https://api.hh.ru/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(HhSearchApi::class.java)
    }

}
