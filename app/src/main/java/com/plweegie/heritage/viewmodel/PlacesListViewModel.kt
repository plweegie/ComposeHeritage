package com.plweegie.heritage.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plweegie.heritage.FeedApi
import com.plweegie.heritage.model.HeritagePlace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PlacesListViewModel @Inject constructor(
    private val placesFeedApi: FeedApi,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val uiState = flow<UiState> {
        emit(UiState.Success(placesFeedApi.getPlacesFeed().placesList
            .filterNot { it.isFreeEntry }
        ))
    }.catch { e ->
        Log.e("PlaceListViewModel", "Error fetching places: $e")
        emit(UiState.Error)
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = UiState.Idle
    )

    sealed class UiState {
        data object Idle : UiState()
        data object Error : UiState()
        data class Success(val places: List<HeritagePlace>) : UiState()
    }
}