package com.dicoding.alficinemacompose.data

import com.dicoding.alficinemacompose.model.BookCinema
import com.dicoding.alficinemacompose.model.CinemaDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map


class CinemaRepository private constructor(
) {
    private val orderCinema = mutableListOf<BookCinema>()

    init {
        if (orderCinema.isEmpty()) {
            CinemaDataSource.dummyCinema.forEach {
                orderCinema.add(BookCinema(it, 0))
            }
        }
    }

    fun searchCinema(query: String): Flow<List<BookCinema>>{
        return flowOf( orderCinema.filter {
            it.cinema.title.contains(query, ignoreCase = true)
        })
    }

    fun getAllCinema(): Flow<List<BookCinema>> {
        return flowOf(orderCinema)
    }

    fun getOrderCinemaById(cinemaId: Long): BookCinema {
        return orderCinema.first {
            it.cinema.id == cinemaId
        }
    }

    fun updateOrderCinema(cinemaId: Long, newCountValue: Int): Flow<Boolean> {
        val index = orderCinema.indexOfFirst { it.cinema.id == cinemaId }
        val result = if (index >= 0) {
            val ordercinema = orderCinema[index]
            orderCinema[index] =
                ordercinema.copy(cinema = ordercinema.cinema, count = newCountValue)
            true
        } else {
            false
        }
        return flowOf(result)
    }

    fun getAddedOrderCinema(): Flow<List<BookCinema>> {
        return getAllCinema()
            .map { orderCinemas ->
                orderCinemas.filter { orderCinema ->
                    orderCinema.count != 0
                }
            }
    }

    companion object {
        @Volatile
        private var instance: CinemaRepository? = null
        fun getInstance(
        ): CinemaRepository =
            instance ?: synchronized(this) {
                instance ?: CinemaRepository()
            }.also { instance = it }
    }
}