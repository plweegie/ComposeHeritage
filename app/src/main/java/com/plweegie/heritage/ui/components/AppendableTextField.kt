package com.plweegie.heritage.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.plweegie.heritage.viewmodel.PlaceDetailViewModel
import kotlinx.coroutines.flow.onCompletion

@Composable
fun AppendableTextField(
    viewModel: PlaceDetailViewModel,
    placeName: String
) {
    var text by rememberSaveable { mutableStateOf("") }
    var collectionCompleted by rememberSaveable { mutableStateOf(false) }

    Text(text = text)

    if (!collectionCompleted) {
        LaunchedEffect(Unit) {
            viewModel.generatePlaceDescription(placeName).onCompletion {
                collectionCompleted = true
            }.collect {
                text += it
            }
        }
    }
}