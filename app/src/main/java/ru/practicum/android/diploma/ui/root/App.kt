package ru.practicum.android.diploma.ui.root

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import ru.practicum.android.diploma.di.dataModule
import ru.practicum.android.diploma.di.interactorModule
import ru.practicum.android.diploma.di.repositoryModule
import ru.practicum.android.diploma.di.uiModule
import ru.practicum.android.diploma.di.viewModelModule
import ru.practicum.android.diploma.domain.impl.UpdateDbOnAppStartUseCase
import ru.practicum.android.diploma.domain.models.FirebaseEvent
import ru.practicum.android.diploma.domain.repository.FirebaseInteractor
import ru.practicum.android.diploma.domain.repository.NetworkConnectionCheckerInteractor

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        // Функция, которая настраивает библиотеку Koin, нужно вызвать перед использованием
        startKoin {
            // Метод специального класса, переданного как this, для добавления контекста в граф
            androidContext(this@App)
            // Передаём все модули, чтобы их содержимое было передано в граф
            modules(repositoryModule, interactorModule, viewModelModule, dataModule, uiModule)
        }

        val dataUpdater: UpdateDbOnAppStartUseCase by inject()

        val firebaseLog: FirebaseInteractor by inject()

        val scope = CoroutineScope(Dispatchers.IO)

        val networkChecker: NetworkConnectionCheckerInteractor by inject()

        scope.launch {
            var isUpdating = false
            var isUpdateSuccess = false
            val childScope = CoroutineScope(Dispatchers.IO)

            childScope.launch {
                networkChecker.onStateChange().collect { isConnected ->
                    if (isConnected && !isUpdating && !isUpdateSuccess) {
                        isUpdating = true
                        runCatching {
                            isUpdateSuccess = dataUpdater()
                            childScope.cancel()
                        }.onSuccess {
                            firebaseLog.logEvent(FirebaseEvent.Log("Areas updated. DB is ok"))
                            isUpdating = false
                        }.onFailure { er ->
                            firebaseLog.logEvent(FirebaseEvent.Error("Start Update: $er"))
                            childScope.cancel()
                        }
                    }
                }
            }
        }
    }
}
