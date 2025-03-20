package org.cidadeape.monitoraorcamento.data.model.empenhos

import kotlinx.serialization.Serializable

@Serializable
data class Empenho(
    val codEmpresa: String?,
    val nomEmpresa: String?,
    val mesEmpenho: String?,
    val datEmpenho: String?,
    val codProjetoAtividade: String?,
    val codFonteRecurso: String?,
    val txDescricaoFonteRecurso: String?,
    val codOrgao: String?,
    val txDescricaoOrgao: String?,
    val codUnidade: String?,
    val txDescricaoUnidade: String?,
    val codElemento: String?,
    val txDescricaoElemento: String?,
    val valTotalEmpenhado: Float,
    val valAnuladoEmpenho: Float,
    val valEmpenhadoLiquido: Float,
    val valLiquidado: Float,
    val valPagoExercicio: Float,
    val valPagoRestos: Float,
    val anexos: List<Anexo>?
)
