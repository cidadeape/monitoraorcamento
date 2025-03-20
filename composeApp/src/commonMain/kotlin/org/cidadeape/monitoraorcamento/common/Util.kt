package org.cidadeape.monitoraorcamento.common

import kotlin.math.roundToInt

object Util {

    fun formatToCurrency(double: Double): String {
        val value = double.roundToInt().toString()

        return "R$ " + value
            .reversed()
            .chunked(3)
            .joinToString(".")
            .reversed()
    }

    fun formatToCurrency(float: Float): String {
        val value = float.roundToInt().toString()

        return "R$ " + value
            .reversed()
            .chunked(3)
            .joinToString(".")
            .reversed()
    }
}