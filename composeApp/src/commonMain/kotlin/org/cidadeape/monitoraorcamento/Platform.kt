package org.cidadeape.monitoraorcamento

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform