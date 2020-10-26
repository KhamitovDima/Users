package com.h.users.di.module

import com.h.users.di.annotation.FragmentScope
import com.h.users.ui.MainFragment
import com.h.users.ui.UserFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributesMainFragment() : MainFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributesUserFragment() : UserFragment
}