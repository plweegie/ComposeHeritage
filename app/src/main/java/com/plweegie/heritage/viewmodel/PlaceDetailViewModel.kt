package com.plweegie.heritage.viewmodel

import androidx.lifecycle.ViewModel
import com.plweegie.heritage.domain.GeneratePlaceDescriptionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlaceDetailViewModel @Inject constructor(
    private val placeDescriptionUseCase: GeneratePlaceDescriptionUseCase
) : ViewModel() {

    fun generatePlaceDescription(placeName: String) =
        placeDescriptionUseCase.generatePlaceDescription(placeName)
}