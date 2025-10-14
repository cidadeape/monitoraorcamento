package org.cidadeape.monitoraorcamento.data

import org.cidadeape.monitoraorcamento.data.model.CategoriaDespesa
import org.cidadeape.monitoraorcamento.data.model.despesa.DespesaResponse
import org.cidadeape.monitoraorcamento.data.model.empenhos.Empenho
import org.cidadeape.monitoraorcamento.data.model.projetosAtividades.ProjetosAtividadesResponse

interface IApiSof {

    var anoDefault: String
    var mesDefault: String

    suspend fun getEmpenhos(
        ano: String = anoDefault,
        mes: String = mesDefault,
        codProjetoAtividade: String? = null,
        codOrgao: String? = null,
        codFonteRecurso: String? = null,
        codReferencia: String? = null,
        codDestinacaoRecurso: String? = null,
        codVinculacaoRecurso: String? = null
    ): List<Empenho>

    suspend fun getDespesas(
        ano: String = anoDefault,
        mes: String = mesDefault,
        codProjetoAtividade: String,
        categoriaDespesa: CategoriaDespesa? = null
    ): DespesaResponse

    suspend fun getProjetoAtividade(
        ano: String = anoDefault,
        codProjetoAtividade: String?
    ): ProjetosAtividadesResponse

}