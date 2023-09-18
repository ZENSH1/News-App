package com.xs.firenotesapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xs.firenotesapp.ui.theme.FireNotesAppTheme

@Composable
fun MySnackBar(
    snackBarHostState: SnackbarHostState,
    onAction: () -> Unit
){
    SnackbarHost(hostState = snackBarHostState) {
        Card(
            modifier = Modifier.padding(1.dp),
            elevation = CardDefaults.cardElevation(10.dp),
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 5.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(text = it.visuals.message, textAlign = TextAlign.Center)
                if (it.visuals.actionLabel != null){
                    TextButton(onClick = onAction,
                        modifier = Modifier.fillMaxHeight()) {
                        Text(text = it.visuals.actionLabel!!)
                    }
                }
            }
        }
    }
}

