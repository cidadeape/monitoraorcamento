package org.cidadeape.monitoraorcamento.data.model

import org.cidadeape.monitoraorcamento.data.model.empenhos.Empenho
import org.cidadeape.monitoraorcamento.data.model.projetosAtividades.ProjetoAtividade

val mockListProjetosAtividades = listOf(
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

val mockListEmpenhos = listOf(
    Empenho(
        null,
        null,
        999,
        2025,
        null,
        "2025-01-20",
        "codProcesso",
        "01.001.001/0001-01",
        "Razão Social",
        "123456789",
        "2025",
        "1137",
        "Pavimentação e Recapeamento de Vias",
        "999",
        "FUNDURB",
        "orgao_cod",
        "orgao_desc",
        "cod_unid",
        "desc_uni",
        "cod_elemento",
        "desc_elemento",
        "cod_funcao",
        "desc_funcao",
        "cod_sub_funcao",
        "desc_sub_funcao",
        "cod_item_despesa",
        "desc_item_despesa",
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
        998,
        2025,
        null,
        "2025-01-20",
        "codProcesso",
        "01.001.001/0001-01",
        "Razão Social",
        "123456789",
        "2025",
        "1137",
        "Pavimentação e Recapeamento de Vias",
        "999",
        "FUNDURB",
        "orgao_cod",
        "orgao_desc",
        "cod_unid",
        "desc_uni",
        "cod_elemento",
        "desc_elemento",
        "cod_funcao",
        "desc_funcao",
        "cod_sub_funcao",
        "desc_sub_funcao",
        "cod_item_despesa",
        "desc_item_despesa",
        13000000.5f,
        0f,
        13000000.5f,
        0f,
        0f,
        0f,
        null
    )
)