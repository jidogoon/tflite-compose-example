package com.jidogoon.compose.tflite.example.viewmodel

import androidx.lifecycle.*
import com.jidogoon.compose.tflite.example.model.ModelLoader
import com.jidogoon.compose.tflite.example.model.MovieItem
import com.jidogoon.compose.tflite.example.model.RecommendationClient
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction2

class TFLiteViewModelFactory(
    private val modelLoader: ModelLoader,
    private val recommendationClient: RecommendationClient
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TFLiteViewModel::class.java)) {
            return TFLiteViewModel(recommendationClient, modelLoader.loadMovieList()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

typealias onToggleCallback = KFunction2<Int, Boolean, Unit>

class TFLiteViewModel(private val recommendationClient: RecommendationClient, movies: List<MovieItem>) : ViewModel() {
    private val _allMovies = MutableLiveData(movies)
    private val _inferResult = MutableLiveData<List<RecommendationClient.Result>>(ArrayList())

    val allMovies: LiveData<List<MovieItem>> = Transformations.map(_allMovies) { it }
    val selectedMovies: LiveData<List<MovieItem>> = Transformations.map(_allMovies) {
        _allMovies.value?.filter { it.selected }
    }
    val inferResult: LiveData<String> = Transformations.map(_inferResult) {
        it.joinToString("\n") { result -> result.item.title }
    }

    fun onToggleItem(id: Int, checked: Boolean) {
        if (!checked && selectedMovies.value?.size ?: 0 > 5)
            return
        _allMovies.value?.first { it.id == id }?.selected = !checked
        _allMovies.value = allMovies.value
        GlobalScope.launch {
            val inferResult = inference()
            _inferResult.postValue(inferResult)
        }
    }

    private fun inference(): List<RecommendationClient.Result> {
        selectedMovies.value?.let {
            return recommendationClient.recommend(it)
        }
        return ArrayList()
    }
}