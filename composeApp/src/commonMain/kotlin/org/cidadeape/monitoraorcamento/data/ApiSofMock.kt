package org.cidadeape.monitoraorcamento.data

import org.cidadeape.monitoraorcamento.data.model.Metadados
import org.cidadeape.monitoraorcamento.data.model.empenhos.Empenho
import org.cidadeape.monitoraorcamento.data.model.empenhos.EmpenhoResponse
import org.cidadeape.monitoraorcamento.data.model.projetosAtividades.ProjetoAtividade
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
            listOf(
                Empenho(
                    null,
                    null,
                    null,
                    "2025-01-20",
                    "1137",
                    "999",
                    "FUNDURB",
                    "orgao_cod",
                    "orgao_desc",
                    "cod_unid",
                    "desc_uni",
                    "cod_elemento",
                    "desc_elemento",
                    12000000.5f,
                    0f,
                    12000000.5f,
                    0f,
                    0f,
                    0f,
                    null
                ),

                Empenho(
                    null,
                    null,
                    null,
                    "2025-01-20",
                    "1137",
                    "999",
                    "FUNDURB",
                    "orgao_cod",
                    "orgao_desc",
                    "cod_unid",
                    "desc_uni",
                    "cod_elemento",
                    "desc_elemento",
                    13000000.5f,
                    0f,
                    13000000.5f,
                    0f,
                    0f,
                    0f,
                    null
                )
            )
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
            if (codProjetoAtividade == null) projetosAtividades
            else projetosAtividades.filter { it.codProjetoAtividade == codProjetoAtividade }
        )
    }

    private val projetosAtividades = listOf(
        ProjetoAtividade(
            codProjetoAtividade = "1169",
            "Reforma e Acessibilidade em Passeios Públicos"
        ),
        ProjetoAtividade(
            codProjetoAtividade = "1137",
            "Pavimentação e Recapeamento de Vias"
        ),
        ProjetoAtividade(
            codProjetoAtividade = "1097",
            "Construção de Ciclovias, Ciclofaixas e Ciclorrotas"
        ),
        ProjetoAtividade(
            codProjetoAtividade = "1098",
            "Ampliação, Reforma e Requalificação de Ciclovias, Ciclofaixas e Ciclorrotas"
        ),
        ProjetoAtividade(
            codProjetoAtividade = "1099",
            "Implantação e Construção de Corredores de Ônibus"
        ),
        ProjetoAtividade(
            codProjetoAtividade = "2095",
            "Incentivo ao Uso de Bicicleta - Programa Bike SP - Lei nº 16.547/2016"
        ),
        ProjetoAtividade(
            codProjetoAtividade = "3757",
            "Implantação de Projetos de Redesenho Urbano para Segurança Viária"
        )
    )
}
