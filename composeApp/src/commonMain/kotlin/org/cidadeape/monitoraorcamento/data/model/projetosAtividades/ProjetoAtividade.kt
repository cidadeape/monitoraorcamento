package org.cidadeape.monitoraorcamento.data.model.projetosAtividades

import kotlinx.serialization.Serializable

@Serializable
data class ProjetoAtividade (
    val codProjetoAtividade: String,
    val txtDescricaoProjetoAtividade: String
)
