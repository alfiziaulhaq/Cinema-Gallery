package com.dicoding.alficinemacompose.ui.screen.home

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dicoding.alficinemacompose.data.Injection
import com.dicoding.alficinemacompose.data.ViewModelFactory
import com.dicoding.alficinemacompose.model.BookCinema
import com.dicoding.alficinemacompose.ui.common.UiState
import com.dicoding.alficinemacompose.ui.components.CinemaItem
import com.dicoding.alficinemacompose.ui.components.SearchBar

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    ),
    navigateToDetail: (Long) -> Unit,
) {
    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.getAllCinema()
            }

            is UiState.Success -> {
                HomeContent(
                    book = uiState.data,
                    modifier = modifier,
                    navigateToDetail = navigateToDetail,
                )
            }

            is UiState.Error -> {
                val TAG = "errors"
                Log.d(TAG, "onFAILED: ${uiState.copy()}")
                Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally){
                    Button(onClick = { viewModel.getAllCinema() },modifier=Modifier.padding(20.dp)) {
                       Text(text = "Retry")
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContent(
    book: List<BookCinema>,
    modifier: Modifier = Modifier,
    navigateToDetail: (Long) -> Unit,
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    ),
) {
    val query by viewModel.query
    viewModel.search(query)
    LazyVerticalGrid(
        columns = GridCells.Adaptive(160.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.testTag("CinemaList")
    ) {
        item {
            SearchBar(
                query = query,
                onQueryChange = viewModel::search,
            )}
        items(book) { data ->
            CinemaItem(
                image = data.cinema.image,
                title = data.cinema.title,
                price = data.cinema.price,
                modifier = Modifier
                    .clickable {
                        navigateToDetail(data.cinema.id)
                    }
                    .animateItemPlacement(tween(durationMillis = 100))
            )
        }
    }
}

