package org.cidadeape.monitoraorcamento.data

import org.cidadeape.monitoraorcamento.data.model.CategoriaDespesa
import org.cidadeape.monitoraorcamento.data.model.despesa.DespesaResponse
import org.cidadeape.monitoraorcamento.data.model.empenhos.EmpenhoResponse
import org.cidadeape.monitoraorcamento.data.model.projetosAtividades.ProjetosAtividadesResponse

interface IApiSof {

    suspend fun getEmpenhos(ano: String, mes: String, codProjetoAtividade: String): EmpenhoResponse

    suspend fun getDespesas(
        ano: String,
        mes: String,
        codProjetoAtividade: String,
        categoriaDespesa: CategoriaDespesa? = null
    ): DespesaResponse

    suspend fun getProjetoAtividade(ano: String, codProjetoAtividade: String?): ProjetosAtividadesResponse

}