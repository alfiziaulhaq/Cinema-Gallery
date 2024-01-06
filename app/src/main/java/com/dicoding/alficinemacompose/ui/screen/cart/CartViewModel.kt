package com.dicoding.alficinemacompose.ui.screen.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.alficinemacompose.data.CinemaRepository
import com.dicoding.alficinemacompose.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel(
    private val repository: CinemaRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<CartState>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<CartState>>
        get() = _uiState

    fun getAddedOrderCinema() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getAddedOrderCinema()
                .collect { orderCinema ->
                    val totalPrice =
                        orderCinema.sumOf{ it.cinema.price * it.count }
                    _uiState.value = UiState.Success(CartState(orderCinema, totalPrice))
                }
        }
    }

    fun updateOrderCinema(cinemaId: Long, count: Int) {
        viewModelScope.launch {
            repository.updateOrderCinema(cinemaId, count)
                .collect { isUpdated ->
                    if (isUpdated) {
                        getAddedOrderCinema()
                    }
                }
        }
    }
}