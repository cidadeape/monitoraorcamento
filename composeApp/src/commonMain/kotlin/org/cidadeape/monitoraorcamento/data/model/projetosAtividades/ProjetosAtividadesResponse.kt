package org.cidadeape.monitoraorcamento.data.model.projetosAtividades

import kotlinx.serialization.Serializable
import org.cidadeape.monitoraorcamento.data.model.Metadados

@Serializable
data class ProjetosAtividadesResponse (
    val metaDados: Metadados,
    val lstProjetosAtividades: List<ProjetoAtividade>
)
