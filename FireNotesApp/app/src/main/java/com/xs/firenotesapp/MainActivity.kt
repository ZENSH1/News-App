@file:OptIn(ExperimentalMaterial3Api::class)

package com.xs.firenotesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import com.xs.firenotesapp.ui.navigation.mainNavHost
import com.xs.firenotesapp.ui.theme.FireNotesAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FireNotesAppTheme {
              mainNavHost()
            }
        }
    }
}

