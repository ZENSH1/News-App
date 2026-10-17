package com.xs.newsapp.core.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp

/** Adds [extra] on every side. */
@Composable
fun PaddingValues.plusAll(extra: Dp): PaddingValues {
    val layoutDirection = LocalLayoutDirection.current
    return PaddingValues(
        start = calculateStartPadding(layoutDirection) + extra,
        top = calculateTopPadding() + extra,
        end = calculateEndPadding(layoutDirection) + extra,
        bottom = calculateBottomPadding() + extra,
    )
}
