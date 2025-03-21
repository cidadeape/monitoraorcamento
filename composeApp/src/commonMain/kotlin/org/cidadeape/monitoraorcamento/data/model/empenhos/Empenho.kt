package org.cidadeape.monitoraorcamento.data.model.empenhos

import kotlinx.serialization.Serializable

@Serializable
data class Empenho(
    val codEmpresa: String?,
    val nomEmpresa: String?,
    val codEmpenho: Int?,
    val anoEmpenho: Int?,
    val mesEmpenho: String?,
    val datEmpenho: String?,
    val codProcesso: String?,
    val numCpfCnpj: String?,
    val txtRazaoSocial: String?,
    val numContrato: String?,
    val anoContrato: String?,
    val codProjetoAtividade: String,
    val txDescricaoProjetoAtividade: String,
    val codFonteRecurso: String?,
    val txDescricaoFonteRecurso: String?,
    val codOrgao: String?,
    val txDescricaoOrgao: String?,
    val codUnidade: String?,
    val txDescricaoUnidade: String?,
    val codElemento: String?,
    val txDescricaoElemento: String?,
    val codFuncao: String?,
    val txDescricaoFuncao: String?,
    val codSubFuncao: String?,
    val txDescricaoSubFuncao: String?,
    val codItemDespesa: String?,
    val txDescricaoItemDespesa: String?,
    val valTotalEmpenhado: Float,
    val valAnuladoEmpenho: Float,
    val valEmpenhadoLiquido: Float,
    val valLiquidado: Float,
    val valPagoExercicio: Float,
    val valPagoRestos: Float,
    val anexos: List<Anexo>?
)
