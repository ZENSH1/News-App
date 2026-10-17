package com.xs.newsapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.xs.newsapp.app.NewsApp
import com.xs.newsapp.core.ui.theme.NewsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // The bottom navigation bar draws its own background down to the screen edge.
            window.isNavigationBarContrastEnforced = false
        }
        setContent {
            NewsTheme {
                NewsApp()
            }
        }
    }
}
