package com.gedhafu.countrypicker.ui.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


@Composable
fun FormTextField(
    modifier: Modifier = Modifier,
    labelText: String? = null,
    placeholderText: String? = labelText,
    labelPositionTop: Boolean = true,
    errorText: Set<String> = emptySet(),
    value: TextFieldValue = TextFieldValue(),
    enabled: Boolean = true,
    readonly: Boolean = false,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    onClickReadonly: ((Boolean) -> Unit)? = null,
    onValueChange: ((String) -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    keyboardActions: KeyboardActions = KeyboardActions(),
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    Column(Modifier.fillMaxWidth(), Arrangement.spacedBy(5.dp), Alignment.Start) {
        if (labelPositionTop) Text(
            labelText ?: "",
            Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.caption,
        )
        OutlinedTextField(
            modifier = modifier,
            placeholder = { Text(placeholderText.orEmpty()) },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            value = value.text,
            onValueChange = { onValueChange?.invoke(it) },
            shape = RoundedCornerShape(14.dp),
            singleLine = true,
            maxLines = 1,
            enabled = enabled,
            readOnly = readonly,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            isError = errorText.isNotEmpty(),
            visualTransformation = visualTransformation,
            interactionSource = remember { MutableInteractionSource() }
                .also { interactionSource ->
                    LaunchedEffect(interactionSource) {
                        interactionSource.interactions.collect {
                            if (it is PressInteraction.Release) {
                                onClickReadonly?.invoke(true)
                            }
                        }
                    }
                }
        )
        errorText.map {
            Text(
                it,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.error,
            )
        }
    }
}
