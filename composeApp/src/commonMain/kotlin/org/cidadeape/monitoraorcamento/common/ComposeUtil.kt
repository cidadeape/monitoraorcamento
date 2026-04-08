package org.cidadeape.monitoraorcamento.common

import androidx.compose.foundation.clickable
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import org.cidadeape.monitoraorcamento.data.model.empenhos.Empenho

fun colorizedText(color: Color, text: String): AnnotatedString {

    return buildAnnotatedString {
        withStyle(style = SpanStyle(color = color)) {
            append(text)
        }
    }

}

@Composable
fun TextTitle(text: String, textAlign: TextAlign = TextAlign.Start, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        textAlign = textAlign,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        text = text
    )
}

@Composable
fun TextTitleValue(title: String, value: String?) {

    var copyToClipboard by remember { mutableStateOf(false) }
    val text = "$title: $value"
    Text(
        modifier = Modifier.clickable {
            copyToClipboard = true
        },
        text = text
    )
    if (copyToClipboard) {
        CopyToClipboard(text)
        copyToClipboard = false
    }
}

@Composable
fun CopyToClipboard(textToCopy: String, message: String = "Valor copiado para área de transferência") {
    LocalClipboardManager.current.setText(AnnotatedString(textToCopy))
    ShowMessage(message)
}

expect object Logger {
    fun e(tag: String, message: String, throwable: Throwable? = null)
    fun d(tag: String, message: String)
    fun i(tag: String, message: String)
}

@Composable
expect fun ShowMessage(message: String)

@Composable
expect fun OpenInNewWindow(url: String)

@Composable
expect fun DownloadCsv(fileName: String, listaEmpenhos: List<Empenho>)

object AppColors {

    val SuperLightGray = Color(0xFFDDDDDD)

    val SuperLightRed = Color(0xFFFFDDDD)

    val SuperLightYellow = Color(0xFFFFE0D0)

    val DarkYellow = Color(0xFF9a7000)

    val DarkGray = Color(0xFF333333)

    val MediumGreen = Color(0xFF569F99)

    val SuperLightBlue = Color(0xFFD0F0FF)

    val MediumBlue = Color(0xFF1A5073)

    val DarkBlue = Color(0xFF1C354C)

}

object AppButtonColors {

    val BlueButton = ButtonColors(
        containerColor = AppColors.MediumBlue,
        contentColor = Color.White,
        disabledContainerColor = AppColors.SuperLightGray,
        disabledContentColor = Color.White
    )

    val GreenButton = ButtonColors(
        containerColor = AppColors.MediumGreen,
        contentColor = Color.White,
        disabledContainerColor = AppColors.SuperLightGray,
        disabledContentColor = Color.White
    )
}
