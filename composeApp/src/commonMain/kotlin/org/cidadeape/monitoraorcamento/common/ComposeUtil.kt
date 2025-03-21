package org.cidadeape.monitoraorcamento.common

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

fun colorizedText(color: Color, text: String): AnnotatedString {

    return buildAnnotatedString {
        withStyle(style = SpanStyle(color = color)) {
            append(text)
        }
    }

}

@Composable
fun TextTitle(text: String) {
    Text(
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        text = text
    )
}

@Composable
fun TextTitleValue(title: String, value: String?) {
    val clipboardManager = LocalClipboardManager.current

    val text = "$title: $value"
    Text(
        modifier = Modifier.clickable {
            clipboardManager.setText(AnnotatedString(value ?: ""))
            showMessage("Valor copiado para área de transferência")
        },
        text = text
    )
}

expect fun showMessage(message: String)

object AppColors {

    val SuperLightGray = Color(0xFFDDDDDD)

    val Purple = Color(0xFF6200EE)

}
