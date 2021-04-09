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

import android.text.TextUtils
import java.util.ArrayList

class MovieItem(
    val id: Int = 0, val title: String = "",
    val genres: List<String> = ArrayList(), val count: Int = 0,
    var selected: Boolean = false) {

    override fun toString(): String {
        return String.format(
            "Id: %d, title: %s, genres: %s, count: %d, selected: %s",
            id, title, TextUtils.join(JOINER, genres), count, selected
        )
    }

//    override fun equals(other: Any?): Boolean {
//        print("call equals")
//        return super.equals(other)
//    }

    companion object {
        const val JOINER = " | "
        const val DELIMITER_REGEX = "[|]"
    }
}