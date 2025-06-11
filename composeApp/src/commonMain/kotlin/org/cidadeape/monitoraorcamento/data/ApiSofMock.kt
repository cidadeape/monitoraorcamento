package org.cidadeape.monitoraorcamento.data

import org.cidadeape.monitoraorcamento.data.model.CategoriaDespesa
import org.cidadeape.monitoraorcamento.data.model.Metadados
import org.cidadeape.monitoraorcamento.data.model.despesa.DespesaResponse
import org.cidadeape.monitoraorcamento.data.model.empenhos.EmpenhoResponse
import org.cidadeape.monitoraorcamento.data.model.mockListDespesas
import org.cidadeape.monitoraorcamento.data.model.mockListEmpenhos
import org.cidadeape.monitoraorcamento.data.model.mockListProjetosAtividades
import org.cidadeape.monitoraorcamento.data.model.projetosAtividades.ProjetosAtividadesResponse

class ApiSofMock: IApiSof {

    override suspend fun getEmpenhos(
        ano: String,
        mes: String,
        codProjetoAtividade: String?,
        codOrgao: String?,
        codFonteRecurso: String?,
        codReferencia: String?,
        codDestinacaoRecurso: String?,
        codVinculacaoRecurso: String?
    ): EmpenhoResponse {
        return EmpenhoResponse(
            Metadados(
                txtMensagemErro = "",
                qtdPaginas = 1,
                txtStatus = "Success"
            ),
            mockListEmpenhos
        )
    }

    override suspend fun getDespesas(
        ano: String,
        mes: String,
        codProjetoAtividade: String,
        categoriaDespesa: CategoriaDespesa?
    ): DespesaResponse {
        return DespesaResponse(
            Metadados(
                txtMensagemErro = "",
                qtdPaginas = 1,
                txtStatus = "Success"
            ),
            mockListDespesas
        )
    }

    override suspend fun getProjetoAtividade(
        ano: String,
        codProjetoAtividade: String?
    ): ProjetosAtividadesResponse {
        return ProjetosAtividadesResponse(
            Metadados(
                txtMensagemErro = "",
                qtdPaginas = 1,
                txtStatus = "Success"
            ),
            if (codProjetoAtividade == null) mockListProjetosAtividades
            else mockListProjetosAtividades.filter { it.codProjetoAtividade == codProjetoAtividade }
        )
    }


}
