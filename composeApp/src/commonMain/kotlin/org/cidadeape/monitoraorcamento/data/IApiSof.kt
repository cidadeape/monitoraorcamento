package org.cidadeape.monitoraorcamento.data

import org.cidadeape.monitoraorcamento.data.model.CategoriaDespesa
import org.cidadeape.monitoraorcamento.data.model.despesa.DespesaResponse
import org.cidadeape.monitoraorcamento.data.model.empenhos.Empenho
import org.cidadeape.monitoraorcamento.data.model.projetosAtividades.ProjetosAtividadesResponse

interface IApiSof {

    var ano: String
    var mesDefault: String

    suspend fun getEmpenhos(
        ano: String = this@IApiSof.ano,
        mes: String = mesDefault,
        codProjetoAtividade: String? = null,
        codOrgao: Int? = null,
        codFonteRecurso: String? = null,
        codReferencia: String? = null,
        codDestinacaoRecurso: String? = null,
        codVinculacaoRecurso: String? = null
    ): List<Empenho>

    suspend fun getDespesa(
        ano: String = this@IApiSof.ano,
        mes: String = mesDefault,
        codProjetoAtividade: String? = null,
        codOrgao: Int? = null,
        categoriaDespesa: CategoriaDespesa? = null
    ): DespesaResponse

    suspend fun getProjetoAtividade(
        ano: String = this@IApiSof.ano,
        codProjetoAtividade: String?
    ): ProjetosAtividadesResponse

}