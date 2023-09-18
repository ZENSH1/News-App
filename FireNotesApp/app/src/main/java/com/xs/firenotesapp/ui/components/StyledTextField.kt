@file:OptIn(ExperimentalMaterial3Api::class)

package com.xs.firenotesapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xs.firenotesapp.R
import com.xs.firenotesapp.ui.theme.FireNotesAppTheme

@ExperimentalMaterial3Api
@Composable
fun StyledTextField(
    modifier: Modifier = Modifier.padding(5.dp),
    value: String = "The Value",
    placeholder: String = "PlaceHolder",
    onValueChange: (newValue: String) -> Unit,
    textStyle: TextStyle? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: Int? = null,
    visibility: Boolean = true,
    onVisible: () -> Unit = {}
) {

    OutlinedTextField(
        value = value,
        onValueChange = { newValue -> onValueChange(newValue) },
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.labelMedium,
                color = colorResource(id = R.color.blue)
            )
        },
        modifier = modifier.background(Color.Transparent),
        textStyle = textStyle?: TextStyle.Default,
        label = {
            Row(
                Modifier.background(Color.Transparent),
                Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = leadingIcon ?: Icons.Default.KeyboardArrowDown,
                    contentDescription = "Icon",
                    tint = colorResource(id = R.color.blue)
                )
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.labelMedium,
                    color = colorResource(id = R.color.blueInk),
                    textAlign = TextAlign.Center
                )
            }
        },
        trailingIcon = {
            if (trailingIcon != null) {
                IconButton(onClick = onVisible) {
                    Icon(
                        painter = painterResource(id = trailingIcon),
                        contentDescription = "trailing",
                        tint = colorResource(
                            id = R.color.blueInk
                        )
                    )
                }
            }
        },
        visualTransformation = if (visibility)  VisualTransformation.None else PasswordVisualTransformation() ,
        colors = TextFieldDefaults.textFieldColors(colorResource(id = R.color.blueInk), containerColor = Color.Transparent),
        singleLine = true,
        )
}


@Preview(showBackground = true)
@Composable
fun MyPreview(){
    FireNotesAppTheme {
        StyledTextField(onValueChange = {})
    }
}



