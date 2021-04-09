package com.jidogoon.compose.tflite.example.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jidogoon.compose.tflite.example.model.MovieItem
import com.jidogoon.compose.tflite.example.viewmodel.TFLiteViewModel
import com.jidogoon.compose.tflite.example.viewmodel.onToggleCallback


@Composable
fun MainComposable(viewModel: TFLiteViewModel) {
    Column {
        MovieList(
            movies = viewModel.allMovies.observeAsState(initial = ArrayList()),
            modifier = Modifier
                .weight(3f)
                .fillMaxWidth(),
            onClickItem = viewModel::onToggleItem
        )
        Divider()
        SelectedMovieList(
            movies = viewModel.selectedMovies.observeAsState(initial = ArrayList()),
            modifier = Modifier
                .defaultMinSize(10.dp)
                .fillMaxWidth(),
            onClickItem = viewModel::onToggleItem
        )
        Divider()
        InferResult(
            inferResult = viewModel.inferResult.observeAsState(initial = ""),
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
fun MovieList(movies: State<List<MovieItem>>, modifier: Modifier, onClickItem: onToggleCallback) {
    LazyColumn(modifier = modifier, contentPadding = PaddingValues(top = 16.dp)) {
        items(movies.value, key = { movie -> movie.id }) { movieItem ->
            MovieUIItem(movieItem = movieItem, onClick = {
                onClickItem(movieItem.id, movieItem.selected)
            })
        }
    }
}

@Composable
fun MovieUIItem(movieItem: MovieItem, onClick: () -> Unit) {
    val bgColor = if (movieItem.selected) Color.Cyan else Color.Transparent
    Box(modifier = Modifier
        .defaultMinSize(32.dp)
        .fillMaxWidth()
        .background(color = bgColor)
        .clickable(onClick = onClick)) {
        Box(modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp)) {
            Text(movieItem.title, fontSize = 16.sp)
        }
    }
}

@Composable
fun SelectedMovieList(movies: State<List<MovieItem>>, modifier: Modifier, onClickItem: onToggleCallback) {
    Box(modifier = Modifier.background(Color.Gray.copy(0.5f))) {
        Column(modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp)) {
            Text("Selected", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            LazyColumn(modifier = modifier) {
                items(movies.value, key = { movie -> movie.id }) { movieItem ->
                    SelectedMovieUIItem(movieItem = movieItem, onClick = {
                        onClickItem(movieItem.id, movieItem.selected)
                    })
                }
            }
        }
    }
}

@Composable
fun SelectedMovieUIItem(movieItem: MovieItem, onClick: () -> Unit) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = onClick)) {
        Box(modifier = Modifier.padding(top = 2.dp, bottom = 2.dp)) {
            Text(movieItem.title, fontSize = 12.sp)
        }
    }
}

@Composable
fun Divider() {
    Spacer(modifier = Modifier
        .height(1.dp)
        .fillMaxWidth()
        .background(color = Color.Black.copy(0.12f))
    )
}

@Composable
fun InferResult(inferResult: State<String>, modifier: Modifier) {
    Box(modifier = modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp)) {
        Column {
            Text("Recommendation", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            LazyColumn {
                item {
                    Text(text = inferResult.value, fontSize = 12.sp)
                }
            }
        }
    }
}