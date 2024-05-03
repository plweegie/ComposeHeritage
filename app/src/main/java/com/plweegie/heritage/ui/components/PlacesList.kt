package com.plweegie.heritage.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import com.plweegie.heritage.viewmodel.PlacesListViewModel

@Composable
fun PlacesList(
    modifier: Modifier,
    places: List<String>,
    lazyListState: LazyListState = rememberLazyListState()
) {

    LazyColumn(modifier = modifier, state = lazyListState) {
        items(places.size) { index ->
            Text(text = places[index])
            HorizontalDivider()
        }
    }
}