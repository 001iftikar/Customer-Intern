package com.iftikar.customerintern.di

import com.iftikar.customerintern.data.repository.AuthRepositoryImpl
import com.iftikar.customerintern.data.repository.BookingRepositoryImpl
import com.iftikar.customerintern.domain.repository.AuthRepository
import com.iftikar.customerintern.domain.repository.BookingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl) : AuthRepository

    @Singleton
    @Binds
    abstract fun bindBookingRepository(impl: BookingRepositoryImpl) : BookingRepository
}