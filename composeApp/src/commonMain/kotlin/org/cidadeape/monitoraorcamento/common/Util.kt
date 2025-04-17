package org.cidadeape.monitoraorcamento.common

import kotlin.math.roundToLong

object Util {

    fun formatToCurrency(double: Double): String {
        val value = double.roundToLong().toString()

        return "R$ " + value
            .reversed()
            .chunked(3)
            .joinToString(".")
            .reversed()
    }
}

fun Double.formatToBrasil(): String {
    val replaced = this.toString().replace(".", ",")
    return "\"$replaced\""
}