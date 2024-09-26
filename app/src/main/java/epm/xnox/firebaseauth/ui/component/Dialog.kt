package epm.xnox.firebaseauth.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import epm.xnox.firebaseauth.R

@Composable
fun ChargingDialog(show: Boolean) {
    if (show) {
        Dialog(
            onDismissRequest = { },
            content = {
                Card(
                    elevation = CardDefaults.cardElevation(6.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
                        Spacer(modifier = Modifier.width(15.dp))
                        Text(text = stringResource(id = R.string.dialog_charging))
                    }
                }
            }
        )
    }
}

@Composable
fun DialogCustom(
    title: String? = null,
    show: Boolean,
    content: @Composable () -> Unit,
    onConfirmButton: () -> Unit,
    onDismissButton: (() -> Unit)? = null,
) {
    if (show) {
        Dialog(
            onDismissRequest = {
                if (onDismissButton != null) {
                    onDismissButton()
                }
            },
            content = {
                Card(
                    elevation = CardDefaults.cardElevation(6.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
                ) {
                    if (title != null) {
                        val textStyleTitle = MaterialTheme.typography.headlineSmall
                        ProvideTextStyle(textStyleTitle) {
                            Box(Modifier.padding(16.dp))
                            {
                                Text(text = title)
                            }
                        }
                    }
                    Box { content() }
                    Spacer(modifier = Modifier.height(5.dp))
                    Box(
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(bottom = 10.dp, end = 10.dp)
                    ) {
                        CompositionLocalProvider(LocalContentColor provides AlertDialogDefaults.textContentColor) {
                            val textStyleButton =
                                MaterialTheme.typography.labelMedium
                            Row {
                                if (onDismissButton != null) {
                                    ProvideTextStyle(value = textStyleButton, content = {
                                        TextButton(onClick = { onDismissButton() }) {
                                            ButtonDialog(text = stringResource(id = R.string.dialog_btn_cancel))
                                        }
                                    })
                                }
                                ProvideTextStyle(value = textStyleButton, content = {
                                    TextButton(onClick = { onConfirmButton() }) {
                                        ButtonDialog(text = stringResource(id = R.string.dialog_btn_ok))
                                    }
                                })
                            }

                        }
                    }
                }
            }
        )
    }
}

@Composable
fun ButtonDialog(text: String) {
    Text(text = text, color = MaterialTheme.colorScheme.onBackground)
}
