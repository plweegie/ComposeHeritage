package com.plweegie.heritage.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.plweegie.heritage.ui.theme.HeritageRed

@Composable
fun LoadingIndicator(
    loading: Boolean
) {
    if (!loading) return

    Box(modifier = Modifier.padding(64.dp)) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = HeritageRed,
            trackColor = HeritageRed.copy(alpha = 0.2f)
        )
    }
}