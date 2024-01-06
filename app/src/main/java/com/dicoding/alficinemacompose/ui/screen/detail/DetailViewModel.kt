package com.dicoding.alficinemacompose.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.alficinemacompose.data.CinemaRepository
import com.dicoding.alficinemacompose.model.BookCinema
import com.dicoding.alficinemacompose.model.Cinema
import com.dicoding.alficinemacompose.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: CinemaRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<BookCinema>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<BookCinema>>
        get() = _uiState

    fun getCinemaById(cinemaId: Long) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = UiState.Success(repository.getOrderCinemaById(cinemaId))
        }
    }

    fun addToCart(cinema: Cinema, count: Int) {
        viewModelScope.launch {
            repository.updateOrderCinema(cinema.id, count)
        }
    }
}