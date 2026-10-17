package com.xs.newsapp.feature.publish

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xs.newsapp.R
import com.xs.newsapp.core.mvi.ObserveEffects
import com.xs.newsapp.core.ui.components.FormTextField
import com.xs.newsapp.core.ui.components.LoadingButton
import com.xs.newsapp.core.ui.components.message
import com.xs.newsapp.core.ui.components.messageRes
import com.xs.newsapp.core.ui.theme.NewsTheme
import com.xs.newsapp.core.validation.Validation
import kotlinx.coroutines.launch

@Composable
fun PublishScreen(viewModel: PublishViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val resources = LocalResources.current

    ObserveEffects(viewModel.effects) { effect ->
        val message = when (effect) {
            PublishEffect.Published -> resources.getString(R.string.publish_success)
            is PublishEffect.ShowError -> resources.getString(effect.error.messageRes())
        }
        scope.launch { snackbarHostState.showSnackbar(message) }
    }

    PublishContent(state = state, onIntent = viewModel::onIntent, snackbarHostState = snackbarHostState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublishContent(
    state: PublishState,
    onIntent: (PublishIntent) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    Scaffold(
        modifier = modifier,
        topBar = { CenterAlignedTopAppBar(title = { Text(stringResource(R.string.publish_title)) }) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        // Includes the keyboard.
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            FormTextField(
                value = state.headline,
                onValueChange = { onIntent(PublishIntent.HeadlineChanged(it)) },
                label = stringResource(R.string.field_headline),
                error = state.headlineError?.message(maxLength = Validation.MAX_HEADLINE_LENGTH),
                supportingText = "${state.headline.length} / ${Validation.MAX_HEADLINE_LENGTH}",
                enabled = !state.isPublishing,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next,
                ),
            )
            FormTextField(
                value = state.description,
                onValueChange = { onIntent(PublishIntent.DescriptionChanged(it)) },
                label = stringResource(R.string.field_description),
                error = state.descriptionError?.message(maxLength = Validation.MAX_DESCRIPTION_LENGTH),
                enabled = !state.isPublishing,
                singleLine = false,
                minLines = 6,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            )
            LoadingButton(
                text = stringResource(R.string.publish_button),
                isLoading = state.isPublishing,
                onClick = {
                    focusManager.clearFocus()
                    onIntent(PublishIntent.Publish)
                },
            )
        }
    }
}

@Preview
@Composable
private fun PublishContentPreview() {
    NewsTheme {
        PublishContent(
            state = PublishState(headline = "Heatwave expected to break", description = "Cooler winds from Sunday."),
            onIntent = {},
            snackbarHostState = SnackbarHostState(),
        )
    }
}
