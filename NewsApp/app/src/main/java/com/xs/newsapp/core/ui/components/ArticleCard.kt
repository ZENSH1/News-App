package com.xs.newsapp.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.xs.newsapp.R
import com.xs.newsapp.data.model.Article
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/** Creates the date formatter once per screen instead of once per list item. */
@Composable
fun rememberArticleDateFormatter(): DateTimeFormatter = remember {
    DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT).withZone(ZoneId.systemDefault())
}

/** One story. Shows a delete button when [onDelete] is given (the author's own stories). */
@Composable
fun ArticleCard(
    article: Article,
    dateFormatter: DateTimeFormatter,
    modifier: Modifier = Modifier,
    onDelete: (() -> Unit)? = null,
) {
    val publishedAt = remember(article.publishedAtEpochMillis, dateFormatter) {
        article.publishedAtEpochMillis.takeIf { it > 0 }?.let { dateFormatter.format(Instant.ofEpochMilli(it)) }
    }
    ElevatedCard(modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(text = article.headline, style = MaterialTheme.typography.titleLarge)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.article_byline, article.authorName),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f),
                )
                if (publishedAt != null) {
                    Text(
                        text = publishedAt,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Text(text = article.description, style = MaterialTheme.typography.bodyMedium)
            if (onDelete != null) {
                TextButton(onClick = onDelete, modifier = Modifier.align(Alignment.End)) {
                    Icon(Icons.Filled.Delete, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.delete))
                }
            }
        }
    }
}
