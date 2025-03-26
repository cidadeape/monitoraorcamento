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
        navigateUp: () -> Unit,
        title: String = "Grupo: ${grupoState.nome}"
    ): Screen(title, canNavigateBack, navigateUp)

    class ProjetoAtividade(
        val projetoAtividade: org.cidadeape.monitoraorcamento.data.model.projetosAtividades.ProjetoAtividade,
        canNavigateBack: Boolean,
        navigateUp: () -> Unit,
        override val title: String = "Projeto ${projetoAtividade.codProjetoAtividade} - ${projetoAtividade.txtDescricaoProjetoAtividade}"
    ): Screen("Projeto", canNavigateBack, navigateUp)

    class Empenho(
        val empenho: org.cidadeape.monitoraorcamento.data.model.empenhos.Empenho,
        canNavigateBack: Boolean,
        navigateUp: () -> Unit,
        override val title: String = "Empenho ${empenho.codEmpenho} / ${empenho.anoEmpenho}"
    ): Screen("Empenho", canNavigateBack, navigateUp)
}