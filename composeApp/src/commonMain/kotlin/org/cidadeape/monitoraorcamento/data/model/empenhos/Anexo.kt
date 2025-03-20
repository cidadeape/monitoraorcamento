package org.cidadeape.monitoraorcamento.data.model.empenhos

import kotlinx.serialization.Serializable

@Serializable
data class Anexo(
    val siglaUnidadeMedida: String,
    val descricaoAnexo: String,
    val qtdeAnexo: Int,
    val valorUnitarioAnexo: Float
)
