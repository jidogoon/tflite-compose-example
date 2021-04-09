/*
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jidogoon.compose.tflite.example.model

import android.content.res.AssetManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.lang.reflect.Type
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors

object FileUtil {
    @Throws(IOException::class)
    fun loadModelFile(assetManager: AssetManager, modelPath: String): MappedByteBuffer {
        assetManager.openFd(modelPath).use { fileDescriptor ->
            FileInputStream(fileDescriptor.fileDescriptor).use { inputStream ->
                val fileChannel: FileChannel = inputStream.channel
                val startOffset: Long = fileDescriptor.startOffset
                val declaredLength: Long = fileDescriptor.declaredLength
                return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
            }
        }
    }

    @Throws(IOException::class)
    fun loadMovieList(
        assetManager: AssetManager, candidateListPath: String
    ): List<MovieItem> {
        val content = loadFileContent(assetManager, candidateListPath)
        val gson = Gson()
        val type: Type = object : TypeToken<List<MovieItem?>?>() {}.type
        return gson.fromJson(content, type)
    }

    @Throws(IOException::class)
    fun loadGenreList(assetManager: AssetManager, genreListPath: String): List<String> {
        val content = loadFileContent(assetManager, genreListPath)
        val lines = content.split(System.lineSeparator()).toTypedArray()
        return listOf(*lines)
    }

    @Throws(IOException::class)
    fun loadConfig(assetManager: AssetManager, configPath: String): Config {
        val content = loadFileContent(assetManager, configPath)
        val gson = Gson()
        val type: Type = object : TypeToken<Config?>() {}.type
        return gson.fromJson(content, type)
    }

    @Throws(IOException::class)
    private fun loadFileContent(assetManager: AssetManager, path: String): String {
        assetManager.open(path).use { ins ->
            BufferedReader(InputStreamReader(ins, StandardCharsets.UTF_8)).use { reader ->
                return reader.lines().collect(
                    Collectors.joining(System.lineSeparator())
                )
            }
        }
    }
}