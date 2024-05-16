package com.plweegie.heritage.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plweegie.heritage.LocationTracker
import com.plweegie.heritage.model.HeritagePlace
import com.plweegie.heritage.model.PlacesRepository
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
    private val repository: PlacesRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val currentLocation
        get() = repository.currentLocation

    private val sortingComparatorState: MutableState<Comparator<HeritagePlace>> =
         if (currentLocation.value == LocationTracker.DEFAULT_LOCATION) {
                mutableStateOf(compareBy { it.title })
            } else {
                mutableStateOf(compareBy { currentLocation.value.distanceTo(it.location) })
         }

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
    private val _comparatorFlow: MutableStateFlow<Comparator<HeritagePlace>> =
        MutableStateFlow(compareBy { it.title })

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState = combine(_regionFilterFlow, _categoryFilterFlow, _comparatorFlow) { region, category, comp ->
        region to category
    }.flatMapLatest { (region, category) ->
        flow<UiState> {
            emit(UiState.Success(repository.getPlacesFeed().placesList
                .sortedWith(sortingComparatorState.value)
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

    suspend fun findCurrentLocation() {
        repository.findCurrentLocation()
    }

    sealed class UiState {
        data object Loading : UiState()
        data object Error : UiState()
        data class Success(val places: List<HeritagePlace>) : UiState()
    }
}