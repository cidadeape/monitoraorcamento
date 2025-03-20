package org.cidadeape.monitoraorcamento.presentation

sealed class Screen(
    val title: String,
    val canNavigateBack: Boolean,
    val navigateUp: () -> Unit
) {

    class Home: Screen("Monitora Orçamento", false, {})

    class ProjetoAtividade(
        val projetoAtividade: org.cidadeape.monitoraorcamento.data.model.projetosAtividades.ProjetoAtividade,
        canNavigateBack: Boolean,
        navigateUp: () -> Unit
    ): Screen("Projeto", canNavigateBack, navigateUp)

    class Empenho(
        val empenho: org.cidadeape.monitoraorcamento.data.model.empenhos.Empenho,
        canNavigateBack: Boolean,
        navigateUp: () -> Unit
    ): Screen("Empenho", canNavigateBack, navigateUp)
}