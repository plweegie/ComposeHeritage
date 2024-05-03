package com.plweegie.heritage.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plweegie.heritage.model.HeritagePlace
import com.plweegie.heritage.xmlfeedparser.HeritageFeedParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParserException
import javax.inject.Inject

@HiltViewModel
class PlacesListViewModel @Inject constructor(
    private val xmlFeedParser: HeritageFeedParser,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _heritagePlacesFlow: MutableStateFlow<UiState> = MutableStateFlow(UiState.Idle)

    val uiState = flow<UiState> {
        xmlFeedParser.parseFeed().toList().let {
            emit(UiState.Success(it))
        }
    }.catch { e ->
        Log.e("PlaceListViewModel", "Error fetching places: $e")
        emit(UiState.Error)
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = UiState.Idle
    )

    fun fetchPlaces() {
        viewModelScope.launch {
            try {
                _heritagePlacesFlow.value = UiState.Success(
                    xmlFeedParser.parseFeed().toList()
                )
            } catch (e: XmlPullParserException) {
                _heritagePlacesFlow.value = UiState.Error
            }
        }
    }

    sealed class UiState {
        data object Idle : UiState()
        data object Error : UiState()
        data class Success(val places: List<HeritagePlace>) : UiState()
    }
}