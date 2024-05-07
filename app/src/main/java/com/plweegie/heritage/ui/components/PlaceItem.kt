package com.plweegie.heritage.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.plweegie.heritage.model.HeritagePlace
import com.plweegie.heritage.ui.theme.HeritageRed

@Composable
fun PlaceItem(
    modifier: Modifier,
    place: HeritagePlace
) {
    Column(modifier = modifier) {
        AsyncImage(
            modifier = Modifier.fillMaxWidth(),
            model = place.imagePath,
            contentScale = ContentScale.FillWidth,
            contentDescription = "Picture of ${place.title}"
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = place.title.uppercase(), color = HeritageRed)
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = place.description, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "${place.county}, ${place.region}", fontSize = 18.sp)
    }
}