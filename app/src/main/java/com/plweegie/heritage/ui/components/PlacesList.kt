package com.plweegie.heritage.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.plweegie.heritage.model.HeritagePlace

@Composable
fun PlacesList(
    modifier: Modifier = Modifier,
    places: List<HeritagePlace>,
    lazyListState: LazyListState = rememberLazyListState()
) {

    LazyColumn(
        modifier = modifier,
        state = lazyListState,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(places.size) { index ->
            PlaceItem(
                modifier = Modifier.padding(vertical = 32.dp),
                place = places[index]
            )
            HorizontalDivider()
        }
    }
}