package org.cidadeape.monitoraorcamento.presentation

import org.cidadeape.monitoraorcamento.presentation.grupo.GrupoState

sealed class Screen(
    open val title: String,
    val canNavigateBack: Boolean,
    val navigateUp: () -> Unit
) {

    class Home: Screen("Monitora Orçamento", false, {})

    class Busca(
        navigateUp: () -> Unit
    ): Screen("Monitora Orçamento - Busca", true, navigateUp)

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