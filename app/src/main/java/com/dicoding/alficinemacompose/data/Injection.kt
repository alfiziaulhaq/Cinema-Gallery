package com.dicoding.alficinemacompose.data

object Injection {
    fun provideRepository(): CinemaRepository {
        return CinemaRepository.getInstance()
    }

}