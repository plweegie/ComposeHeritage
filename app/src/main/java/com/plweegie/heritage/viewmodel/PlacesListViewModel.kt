package com.plweegie.heritage.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plweegie.heritage.FeedApi
import com.plweegie.heritage.LocationTracker
import com.plweegie.heritage.model.HeritagePlace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PlacesListViewModel @Inject constructor(
    private val placesFeedApi: FeedApi,
    private val locationTracker: LocationTracker,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val currentLocation = mutableStateOf(LocationTracker.DEFAULT_LOCATION)

    var regionFilter: String
        get() = _regionFilterFlow.value
        set(value) {
            _regionFilterFlow.value = value
        }

    var categoryFilter: String
        get() = _categoryFilterFlow.value
        set(value) {
            _categoryFilterFlow.value = value
        }

    private val _regionFilterFlow: MutableStateFlow<String> = MutableStateFlow("")
    private val _categoryFilterFlow: MutableStateFlow<String> = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState = combine(_regionFilterFlow, _categoryFilterFlow) { region, category ->
        region to category
    }.flatMapLatest { (region, category) ->
        flow<UiState> {
            emit(UiState.Success(placesFeedApi.getPlacesFeed().placesList
                .filterNot { it.isFreeEntry }
                .filter { it.region == region || region.isEmpty() }
                .filter { it.category == category || category.isEmpty() }
            ))
        }.onStart {
            emit(UiState.Loading)
        }.catch { e ->
            Log.e("PlaceListViewModel", "Error fetching places: $e")
            emit(UiState.Error)
        }
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = UiState.Loading
    )

    suspend fun getCurrentLocation() {
        currentLocation.value = locationTracker.getCurrentLocation()
    }

    sealed class UiState {
        data object Loading : UiState()
        data object Error : UiState()
        data class Success(val places: List<HeritagePlace>) : UiState()
    }
}