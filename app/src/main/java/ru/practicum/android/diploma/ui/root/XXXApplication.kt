package ru.practicum.android.diploma.ui.root

import android.app.Application
import ru.practicum.android.diploma.di.dataModule
import ru.practicum.android.diploma.di.interactorModule
import ru.practicum.android.diploma.di.repositoryModule
import ru.practicum.android.diploma.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class XXXApplication : Application() {

    override fun onCreate() {

        super.onCreate()

        // Функция, которая настраивает библиотеку Koin, нужно вызвать перед использованием
        startKoin {
            // Метод специального класса, переданного как this, для добавления контекста в граф
            androidContext(this@XXXApplication)
            // Передаём все модули, чтобы их содержимое было передано в граф
            modules(repositoryModule, interactorModule, viewModelModule, dataModule)
        }
    }
}
