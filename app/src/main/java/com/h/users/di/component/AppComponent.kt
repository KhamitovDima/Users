package com.h.users.di.component

import android.app.Application
import com.h.users.di.App
import com.h.users.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjection
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = arrayOf(
        AndroidInjectionModule::class,
        ActivityModule::class,
        FragmentModule::class,
        AppModule::class,
        NetworkModule::class,
        ViewModelModule::class
    )
)
interface AppComponent: AndroidInjector<App> {

    fun inject(app: Application)

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun application(app: Application): Builder
    }
}