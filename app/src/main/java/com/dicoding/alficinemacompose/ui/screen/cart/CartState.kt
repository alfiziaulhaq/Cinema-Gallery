package com.dicoding.alficinemacompose.ui.screen.cart

import com.dicoding.alficinemacompose.model.BookCinema

data class CartState(
    val orderCinema: List<BookCinema>,
    val totalPrice: Int
)