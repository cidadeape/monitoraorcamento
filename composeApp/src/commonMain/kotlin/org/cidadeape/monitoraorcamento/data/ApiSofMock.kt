package org.cidadeape.monitoraorcamento.data

import org.cidadeape.monitoraorcamento.data.model.CategoriaDespesa
import org.cidadeape.monitoraorcamento.data.model.Metadados
import org.cidadeape.monitoraorcamento.data.model.despesa.DespesaResponse
import org.cidadeape.monitoraorcamento.data.model.empenhos.Empenho
import org.cidadeape.monitoraorcamento.data.model.mockListDespesas
import org.cidadeape.monitoraorcamento.data.model.mockListEmpenhos
import org.cidadeape.monitoraorcamento.data.model.mockListProjetosAtividades
import org.cidadeape.monitoraorcamento.data.model.projetosAtividades.ProjetosAtividadesResponse

class ApiSofMock: IApiSof {

    override var ano: String = "2025"
    override var mesDefault: String = "12"

    override suspend fun getEmpenhos(
        ano: String,
        mes: String,
        codProjetoAtividade: String?,
        codOrgao: Int?,
        codFonteRecurso: String?,
        codReferencia: String?,
        codDestinacaoRecurso: String?,
        codVinculacaoRecurso: String?
    ): List<Empenho> {
        return mockListEmpenhos
    }

    override suspend fun getDespesa(
        ano: String,
        mes: String,
        codProjetoAtividade: String?,
        codOrgao: Int?,
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
