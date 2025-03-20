package org.cidadeape.monitoraorcamento.data.model.empenhos

import kotlinx.serialization.Serializable
import org.cidadeape.monitoraorcamento.data.model.Metadados

@Serializable
data class EmpenhoResponse (
    val metaDados: Metadados,
    val lstEmpenhos: List<Empenho>
)
