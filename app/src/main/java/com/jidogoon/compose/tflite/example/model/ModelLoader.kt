package com.jidogoon.compose.tflite.example.model

import android.content.res.AssetManager


class ModelLoader(private val assetManager: AssetManager) {
    companion object {
        private const val CONFIG_PATH = "config.json"
    }

    val config: Config = FileUtil.loadConfig(assetManager, CONFIG_PATH)

    fun loadMovieList(): List<MovieItem> {
        return FileUtil.loadMovieList(assetManager, config.movieList)
    }
}