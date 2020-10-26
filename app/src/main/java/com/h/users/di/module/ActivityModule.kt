package com.h.users.di.module

import com.h.users.MainActivity
import com.h.users.di.annotation.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributeMainActivityAndroidInjector(): MainActivity
}