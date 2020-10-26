package com.h.users

import com.h.users.di.component.AppComponent
import dagger.Component
import javax.inject.Singleton

@Component(modules = [RetrofitTestModule::class])
@Singleton
interface TestAppComponent : AppComponent {

}