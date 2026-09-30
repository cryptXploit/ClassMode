package com.classmode.presentation.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.classmode.R
import com.classmode.presentation.theme.LocalHaptic

@Composable
fun DestructiveConfirmationDialog(
    showDialog: Boolean,
    title: String = stringResource(id = R.string.title_warning),
    text: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (!showDialog) return

    val haptic = LocalHaptic.current

    AlertDialog(
        onDismissRequest = {
            haptic.performClickEffect()
            onDismiss()
        },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    haptic.performClickEffect()
                    onConfirm()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text(text = stringResource(id = R.string.action_delete_confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    haptic.performClickEffect()
                    onDismiss()
                }
            ) {
                Text(text = stringResource(id = R.string.action_cancel))
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        textContentColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
