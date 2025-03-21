package org.cidadeape.monitoraorcamento.presentation

sealed class Screen(
    open val title: String,
    val canNavigateBack: Boolean,
    val navigateUp: () -> Unit
) {

    class Home: Screen("Monitora Orçamento", false, {})

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