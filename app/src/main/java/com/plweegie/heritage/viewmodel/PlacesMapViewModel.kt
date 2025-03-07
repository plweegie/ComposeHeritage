package com.plweegie.heritage.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plweegie.heritage.domain.GetCurrentLocationUseCase
import com.plweegie.heritage.domain.GetPlacesUseCase
import com.plweegie.heritage.model.HeritagePlace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PlacesMapViewModel @Inject constructor(
    private val placesUseCase: GetPlacesUseCase,
    private val locationUseCase: GetCurrentLocationUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val currentLocation
        get() = locationUseCase.currentLocation

    val uiState = flow<UiState> {
        emit(UiState.Success(placesUseCase.getPlacesFeed().placesList))
    }.onStart {
        emit(UiState.Loading)
    }.catch { e ->
        Log.e("PlaceMapViewModel", "Error fetching places: $e")
        emit(UiState.Error)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.Loading
    )

    suspend fun findCurrentLocation() {
        locationUseCase.findCurrentLocation()
    }

    sealed class UiState {
        data object Loading : UiState()
        data object Error : UiState()
        data class Success(val places: List<HeritagePlace>) : UiState()
    }
}