package com.jidogoon.compose.tflite.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jidogoon.compose.tflite.example.composables.MainComposable
import com.jidogoon.compose.tflite.example.model.ModelLoader
import com.jidogoon.compose.tflite.example.model.RecommendationClient
import com.jidogoon.compose.tflite.example.viewmodel.TFLiteViewModelFactory


class MainActivity : ComponentActivity() {
    private lateinit var modelLoader: ModelLoader
    private lateinit var recommendationClient: RecommendationClient

    override fun onStart() {
        super.onStart()
        modelLoader = ModelLoader(assets)
        recommendationClient = RecommendationClient(this, modelLoader.config).apply {
            load()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Scaffold(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()) {
                    MainComposable(viewModel(
                        factory = TFLiteViewModelFactory(modelLoader, recommendationClient)
                    ))
                }
            }
        }
    }
}