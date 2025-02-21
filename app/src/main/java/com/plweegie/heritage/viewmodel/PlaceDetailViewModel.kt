package com.plweegie.heritage.viewmodel

import androidx.lifecycle.ViewModel
import com.google.ai.client.generativeai.GenerativeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class PlaceDetailViewModel @Inject constructor(
    private val generativeModel: GenerativeModel
) : ViewModel() {

    private companion object {
        const val PROMPT = "Tell me something about "
    }

    fun generatePlaceDescription(placeName: String): Flow<String> {
        val prompt = PROMPT + placeName
        return generativeModel.generateContentStream(prompt).map {
            it.text
        }.filterNotNull()
    }
}