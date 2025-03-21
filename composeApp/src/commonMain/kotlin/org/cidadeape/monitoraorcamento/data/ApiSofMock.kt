package org.cidadeape.monitoraorcamento.data

import org.cidadeape.monitoraorcamento.data.model.Metadados
import org.cidadeape.monitoraorcamento.data.model.empenhos.EmpenhoResponse
import org.cidadeape.monitoraorcamento.data.model.mockListEmpenhos
import org.cidadeape.monitoraorcamento.data.model.mockListProjetosAtividades
import org.cidadeape.monitoraorcamento.data.model.projetosAtividades.ProjetosAtividadesResponse

class ApiSofMock: IApiSof {

    override suspend fun getEmpenhos(
        ano: String,
        mes: String,
        codProjetoAtividade: String
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
