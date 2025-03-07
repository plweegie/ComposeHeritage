package com.plweegie.heritage.domain

import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class GeneratePlaceDescriptionUseCase(private val generativeModel: GenerativeModel) {

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