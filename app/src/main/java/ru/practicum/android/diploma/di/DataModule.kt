package ru.practicum.android.diploma.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.data.DB_NAME
import ru.practicum.android.diploma.data.DB_VERSION
import ru.practicum.android.diploma.data.db.DbHelper
import ru.practicum.android.diploma.data.db.XxxDataBase
import ru.practicum.android.diploma.data.db.dao.AreasDao
import ru.practicum.android.diploma.data.db.dao.DataLoadingStatusDao
import ru.practicum.android.diploma.data.db.dao.FavoriteVacancyDao
import ru.practicum.android.diploma.data.db.dao.IndustriesDao
import ru.practicum.android.diploma.data.db.dao.SearchFilterDao
import ru.practicum.android.diploma.data.network.HhSearchApiProvider
import ru.practicum.android.diploma.data.network.api.NetworkClient
import ru.practicum.android.diploma.data.network.impl.RetrofitNetworkClient
import ru.practicum.android.diploma.data.network.mapper.NetworkMapper
import ru.practicum.android.diploma.data.sharing.ExternalNavigatorRepositoryImpl
import ru.practicum.android.diploma.domain.sharing.api.ExternalNavigatorRepository

val dataModule = module {

    single<XxxDataBase> {
        Room.databaseBuilder(androidContext(), XxxDataBase::class.java, DB_NAME).build()
    }

    single<DataLoadingStatusDao> {
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

    single<SearchFilterDao> {
        get<XxxDataBase>().searchFilterDao()
    }

    single<FavoriteVacancyDao> {
        get<XxxDataBase>().favoriteVacancyDao()
    }

    single<ConnectivityManager> {
        androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    single<MutableStateFlow<Boolean>> {
        MutableStateFlow(false)
    }

    single<CoroutineScope> {
        CoroutineScope(Dispatchers.IO)
    }

    single<NetworkMapper> {
        NetworkMapper()
    }

    single<NetworkClient> {
        RetrofitNetworkClient(
            apiProvider = get(),
            connectionChecker = get(),
            mapper = get()
        )
    }

    single<HhSearchApiProvider> {
        HhSearchApiProvider(scope = get(), checker = get(), context = androidContext())
    }

    factory<FirebaseAnalytics> {
        FirebaseAnalytics.getInstance(androidContext())
    }
}
