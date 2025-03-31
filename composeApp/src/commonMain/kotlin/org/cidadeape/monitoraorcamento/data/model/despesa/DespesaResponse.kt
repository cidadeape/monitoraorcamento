package org.cidadeape.monitoraorcamento.data.model.despesa

import org.cidadeape.monitoraorcamento.data.model.Metadados

data class DespesaResponse(
    val metadados: Metadados,
    val lstDespesas: List<Despesa>
)
