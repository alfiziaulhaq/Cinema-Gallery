package com.dicoding.alficinemacompose.ui.screen.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.alficinemacompose.data.CinemaRepository
import com.dicoding.alficinemacompose.model.BookCinema
import com.dicoding.alficinemacompose.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: CinemaRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<List<BookCinema>>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<BookCinema>>>
        get() = _uiState

    private val _query = mutableStateOf("")
    val query: State<String> get() = _query

    init {
        getAllCinema()
    }

    fun getAllCinema() {
        viewModelScope.launch {
            repository.getAllCinema()
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect { orderRewards ->
                    _uiState.value = UiState.Success(orderRewards)
                }
        }
    }

    fun search(newQuery: String) {
        viewModelScope.launch {
            _query.value = newQuery
            repository.searchCinema(_query.value)
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect { its ->
                    _uiState.value = UiState.Success(its.sortedBy { it.cinema.title })

                }
        }
    }

}

