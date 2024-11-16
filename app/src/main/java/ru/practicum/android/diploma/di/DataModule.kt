package ru.practicum.android.diploma.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.CACHE_SIZE_BYTES
import ru.practicum.android.diploma.data.DB_NAME
import ru.practicum.android.diploma.data.DB_VERSION
import ru.practicum.android.diploma.data.db.DbHelper
import ru.practicum.android.diploma.data.db.XxxDataBase
import ru.practicum.android.diploma.data.db.dao.AreasDao
import ru.practicum.android.diploma.data.db.dao.CreateDbDao
import ru.practicum.android.diploma.data.db.dao.IndustriesDao
import ru.practicum.android.diploma.data.network.api.HhSearchApi
import ru.practicum.android.diploma.data.network.api.NetworkClient
import ru.practicum.android.diploma.data.network.api.NetworkConnectionChecker
import ru.practicum.android.diploma.data.network.impl.NetworkConnectionCheckerImpl
import ru.practicum.android.diploma.data.network.impl.RetrofitNetworkClient
import ru.practicum.android.diploma.data.network.mapper.NetworkMapper
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

val dataModule = module {

    single<XxxDataBase> {
        Room.databaseBuilder(androidContext(), XxxDataBase::class.java, DB_NAME).build()
    }

    factory<CreateDbDao> {
        get<XxxDataBase>().createDb()
    }

    factory<DbHelper> {
        DbHelper(androidContext(), DB_NAME, DB_VERSION)
    }

    single<IndustriesDao> {
        get<XxxDataBase>().industriesDao()
    }

    single<AreasDao> {
        get<XxxDataBase>().areasDao()
    }

    single<ConnectivityManager> {
        androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    single<NetworkConnectionChecker> {
        NetworkConnectionCheckerImpl(
            connectivityManager = get()
        )
    }

    single<NetworkMapper> {
        NetworkMapper()
    }

    single<NetworkClient> {
        RetrofitNetworkClient(
            hhSearchApi = get(),
            connectionChecker = get(),
            mapper = get()
        )
    }

    single<HhSearchApi> {
        val interceptor = Interceptor { chain ->

            val token = BuildConfig.HH_ACCESS_TOKEN
            val appname = "Навигатор Вакансий XXX"

            val appNameUrl = URLEncoder.encode(appname, StandardCharsets.UTF_8.toString())
            //val appNameUrl = "Navigator Vakansij XXX"
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
            .cache(Cache(androidContext().cacheDir, CACHE_SIZE_BYTES))
            .addInterceptor(interceptor)
            .build()

        Retrofit
            .Builder()
            .baseUrl("https://api.hh.ru/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(HhSearchApi::class.java)
    }
}
