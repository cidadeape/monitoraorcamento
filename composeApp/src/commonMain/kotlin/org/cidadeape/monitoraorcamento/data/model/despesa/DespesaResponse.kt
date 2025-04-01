package org.cidadeape.monitoraorcamento.data.model.despesa

import kotlinx.serialization.Serializable
import org.cidadeape.monitoraorcamento.data.model.Metadados

@Serializable
data class DespesaResponse(
    val metaDados: Metadados,
    val lstDespesas: List<Despesa>
)
