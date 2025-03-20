package org.cidadeape.monitoraorcamento.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Metadados(
    val txtMensagemErro: String,
    val qtdPaginas: Int?,
    val txtStatus: String?,
)
