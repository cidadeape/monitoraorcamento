package org.cidadeape.monitoraorcamento.presentation.screen

sealed class Screen(
    open val title: String,
    val canNavigateBack: Boolean,
    val navigateUp: () -> Unit
) {

    class Home: Screen("Monitoramento do Orçamento Municipal - 2025", false, {})

    class Fundurb: Screen("FUNDURB", false, {})

    class BuscaProjeto(
        navigateUp: () -> Unit
    ): Screen("Monitora Orçamento - Busca Projetos / Atividades", true, navigateUp)

    class BuscaEmpenho(
        navigateUp: () -> Unit
    ): Screen("Monitora Orçamento - Busca Empenhos", true, navigateUp)

    class Grupo(
        val grupoState: GrupoState,
        canNavigateBack: Boolean,
        navigateUp: () -> Unit
    ): Screen(
        title = "${grupoState.nome} - Projetos / Atividades",
        canNavigateBack = canNavigateBack,
        navigateUp = navigateUp
    )

    class ProjetoAtividade(
        val projetoAtividade: org.cidadeape.monitoraorcamento.data.model.projetosAtividades.ProjetoAtividade,
        val listaEmpenhos: List<org.cidadeape.monitoraorcamento.data.model.empenhos.Empenho>?,
        canNavigateBack: Boolean,
        navigateUp: () -> Unit
    ): Screen(
        title = "Empenhos - ${projetoAtividade.txtDescricaoProjetoAtividade}",
        canNavigateBack = canNavigateBack,
        navigateUp = navigateUp
    )

    class Empenho(
        val empenho: org.cidadeape.monitoraorcamento.data.model.empenhos.Empenho,
        canNavigateBack: Boolean,
        navigateUp: () -> Unit
    ): Screen(
        title = "Empenho ${empenho.codEmpenho} / ${empenho.anoEmpenho}",
        canNavigateBack = canNavigateBack,
        navigateUp = navigateUp
    )
}