package epm.xnox.firebaseauth.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import epm.xnox.firebaseauth.R

@Composable
fun AuthTextField(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    error: String = "",
    isError: Boolean = false,
    value: String,
    label: String,
    color: Color,
    leadingIcon: ImageVector,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChange: (value: String) -> Unit
) {
    var showPassword by remember { mutableStateOf(false) }

    Column {
        TextField(
            modifier = modifier,
            value = value,
            enabled = enabled,
            isError = isError,
            label = { Text(text = label) },
            keyboardOptions = keyboardOptions,
            onValueChange = { onValueChange(it) }, colors = TextFieldDefaults.colors(
                unfocusedLabelColor = color,
                focusedLabelColor = color,
                cursorColor = color,
                focusedTextColor = color,
                unfocusedTextColor = color,
                disabledLabelColor = color,
                disabledLeadingIconColor = color,
                errorIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorContainerColor = color.copy(alpha = 0.2f),
                focusedContainerColor = color.copy(alpha = 0.2f),
                unfocusedContainerColor = color.copy(alpha = 0.2f),
                disabledContainerColor = color.copy(alpha = 0.2f),
            ),
            shape = RoundedCornerShape(18.dp),
            leadingIcon = {
                Icon(imageVector = leadingIcon, contentDescription = null)
            },
            trailingIcon = {
                if (keyboardOptions.keyboardType == KeyboardType.Password) {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            painter = painterResource(
                                id = if (showPassword) R.drawable.outline_visibility_off
                                else R.drawable.outline_visibility,
                            ),
                            contentDescription = null,
                            tint = color
                        )
                    }
                }
            },
            visualTransformation = if (keyboardOptions.keyboardType == KeyboardType.Password) {
                if (showPassword) VisualTransformation.None else PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            singleLine = true
        )
        if (isError) {
            Text(text = error, color = Color.Red.copy(alpha = 0.5f), fontSize = 14.sp)
        }
    }
}