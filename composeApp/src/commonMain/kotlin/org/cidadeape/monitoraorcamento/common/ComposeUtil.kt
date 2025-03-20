package org.cidadeape.monitoraorcamento.common

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

fun colorizedText(color: Color, text: String): AnnotatedString {

    return buildAnnotatedString {
        withStyle(style = SpanStyle(color = color)) {
            append(text)
        }
    }

}

object AppColors {

    val SuperLightGray = Color(0xFFDDDDDD)

    val Purple = Color(0xFF6200EE)

}
