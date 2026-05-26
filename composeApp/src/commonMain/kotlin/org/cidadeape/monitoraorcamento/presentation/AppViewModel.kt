package org.cidadeape.monitoraorcamento.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.cidadeape.monitoraorcamento.common.LoadingState
import org.cidadeape.monitoraorcamento.data.ApiSof
import org.cidadeape.monitoraorcamento.data.IApiSof
import org.cidadeape.monitoraorcamento.data.model.empenhos.Empenho
import org.cidadeape.monitoraorcamento.data.model.projetosAtividades.ProjetoAtividade
import org.cidadeape.monitoraorcamento.domain.model.TotalDespesas
import org.cidadeape.monitoraorcamento.presentation.screen.GrupoState
import org.cidadeape.monitoraorcamento.presentation.screen.ProjetoAtividadeRowState
import org.cidadeape.monitoraorcamento.presentation.screen.Screen
import kotlin.reflect.KClass

class AppViewModel(
    private val sofApi: IApiSof = ApiSof()
): ViewModel() {

    private val grupoAsfalto = GrupoState(
        nome = "Asfalto",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeRowState("1137"),
            ProjetoAtividadeRowState("2340")
        ))

    private val grupoObrasViarias = GrupoState(
        nome = "Obras Viárias",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeRowState("5100"),
            ProjetoAtividadeRowState("5105")
        ))

    private val grupoCalcadas = GrupoState(
        nome = "Reforma de calçadas",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeRowState("1169")
        ))

    private val grupoBicicleta = GrupoState(
        nome = "Ciclovias e Ciclofaixas",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeRowState("1097"),
            ProjetoAtividadeRowState("1098"),
            ProjetoAtividadeRowState("2093"),
            ProjetoAtividadeRowState("2098")
        ))

    private val grupoSegurancaViaria = GrupoState(
        nome = "Projetos de Segurança Viária",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeRowState("3757"),
            ProjetoAtividadeRowState("3664")
        ))

    private val grupoFiscalizacao = GrupoState(
        nome = "Fiscalização do trânsito",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeRowState("4656"),
            ProjetoAtividadeRowState("4703")
        ))

    private val grupoOnibusInvestimentos = GrupoState(
        nome = "Sistema de Ônibus - Investimentos",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeRowState("1800"), // Eletrificação da frota

            ProjetoAtividadeRowState("1099"), // Contrução e Implantação de Corredores de Ônibus
            ProjetoAtividadeRowState("5392"), // Implantação de Corredores de Ônibus Novos
            ProjetoAtividadeRowState("1100"), // Acess, Ampliação e Reforma de Corredores
            ProjetoAtividadeRowState("5391"), // Construção e implantação de faixas exclusivas
            ProjetoAtividadeRowState("5394"), // Acessibilidade, Ampliação, Reforma e Requalificação de Faixas Exclusivas de Ônibus, inclusive Área d

            ProjetoAtividadeRowState("1095"), // Construção e Implantação de Terminais de Ônibus
            ProjetoAtividadeRowState("1096"), // Acessibilidade, Ampliação, Reforma e Requalificação de Terminais

        ))

    private val grupoOnibusOperacao = GrupoState(
        nome = "Sistema de Ônibus - Operação",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeRowState("4701"), // Compensação tarifária

            ProjetoAtividadeRowState("2099"), // Manutenção e Operação de Corredores
            ProjetoAtividadeRowState("4662"), // Manutenção e Operação de Faixas Exclusivas de Ônibus
            ProjetoAtividadeRowState("4663"), // Contraprestação PPP - Terminais Urbanos
       ))

    private val grupoMeioAmbiente = GrupoState(
        nome = "Meio ambiente",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeRowState("1702"),
            ProjetoAtividadeRowState("1703"),
            ProjetoAtividadeRowState("1704"),
            ProjetoAtividadeRowState("1705"),
            ProjetoAtividadeRowState("1706"),
            ProjetoAtividadeRowState("2324"),
            ProjetoAtividadeRowState("2386"),
            ProjetoAtividadeRowState("2702"),
            ProjetoAtividadeRowState("2703"),
            ProjetoAtividadeRowState("2704"),
            ProjetoAtividadeRowState("2705"),
            ProjetoAtividadeRowState("3355"),
            ProjetoAtividadeRowState("5087"),
            ProjetoAtividadeRowState("6007"),
            ProjetoAtividadeRowState("6009"),
            ProjetoAtividadeRowState("6010"),
            ProjetoAtividadeRowState("6651"),
            ProjetoAtividadeRowState("6655"),
            ProjetoAtividadeRowState("6659"),
            ProjetoAtividadeRowState("6660"),
            ProjetoAtividadeRowState("6663"),
            ProjetoAtividadeRowState("6669"),
            ProjetoAtividadeRowState("6681"),
            ProjetoAtividadeRowState("6682"),
            ProjetoAtividadeRowState("6686"),
            ProjetoAtividadeRowState("7117"),
            ProjetoAtividadeRowState("7127")
        ))

    val listaGrupos = listOf(
        grupoCalcadas,
        grupoSegurancaViaria,
        grupoAsfalto,
        grupoObrasViarias,
        grupoFiscalizacao,
        grupoBicicleta,
        grupoOnibusInvestimentos,
        grupoOnibusOperacao,
        grupoMeioAmbiente
    )

    val listaCustomizadaProjetosAtividades = mutableStateListOf<ProjetoAtividadeRowState>()

    val fullList: MutableStateFlow<LoadingState<List<ProjetoAtividade>>> = MutableStateFlow(
        LoadingState.NotStarted())

    init {
        load()
    }

    fun load() {
        loadAllGrupos()

//        launchCoroutine {
//            loadSearchList()
//        }
    }

    private fun loadAllGrupos() = launchCoroutine {
        for (grupo in listaGrupos) {
            loadGrupo(grupo)
        }
    }

    fun reloadGrupo(grupoState: GrupoState) = launchCoroutine {
        loadGrupo(grupoState)
    }

    suspend fun loadGrupo(grupoState: GrupoState) {
        grupoState.refreshing.value = true
        grupoState.stateOrcadoAtualizado.value = LoadingState.Loading()
        grupoState.stateEmpenhadoLiquido.value = LoadingState.Loading()
        grupoState.statePago.value = LoadingState.Loading()

        for (projAtiv in grupoState.listaProjetosAtividades) {
            loadProjetoAtividadeComTotalDespesas(projAtiv)
        }

        var orcadoAtualizadoGrupo = 0.0
        var empenhadoLiquidoGrupo = 0.0
        var pagoGrupo = 0.0

        var failedDespesasTotal = false

        grupoState.listaProjetosAtividades.forEach {

            failedDespesasTotal = when (val state = it.stateTotalDespesas.value) {
                is LoadingState.Success -> {
                    orcadoAtualizadoGrupo += state.response.orcadoAtualizado
                    empenhadoLiquidoGrupo += state.response.empenhadoLiquido
                    pagoGrupo += state.response.pago
                    false
                }
                else -> true
            }
        }

        if (failedDespesasTotal) {
            grupoState.stateOrcadoAtualizado.value = LoadingState.Failure("Erro ao carregar: Orçado Atualizado")
            grupoState.stateEmpenhadoLiquido.value = LoadingState.Failure("Erro ao carregar: Empenhado Líquido")
            grupoState.statePago.value = LoadingState.Failure("Erro ao carregar: Pago")
        } else {
            grupoState.stateOrcadoAtualizado.value = LoadingState.Success(orcadoAtualizadoGrupo)
            grupoState.stateEmpenhadoLiquido.value = LoadingState.Success(empenhadoLiquidoGrupo)
            grupoState.statePago.value = LoadingState.Success(pagoGrupo)
        }

        grupoState.refreshing.value = false
    }

    fun removerDaListaCustomizada(projetoAtividadeRowState: ProjetoAtividadeRowState) {

        listaCustomizadaProjetosAtividades.remove(projetoAtividadeRowState)
    }

    fun adicionarAListaCustomizada(projetoAtividade: ProjetoAtividade) {

        if (listaCustomizadaProjetosAtividades
            .map { it.codigo }
            .contains(projetoAtividade.codProjetoAtividade)
        ) return

        val projetoAtividadeRowState = ProjetoAtividadeRowState(
            codigo = projetoAtividade.codProjetoAtividade,
            stateProjeto = MutableStateFlow(LoadingState.Success(projetoAtividade))
        )

        listaCustomizadaProjetosAtividades.add(0, projetoAtividadeRowState)
        launchCoroutine {
            loadProjetoAtividadeComTotalDespesas(projetoAtividadeRowState)
        }
    }

    private suspend fun loadSearchList() {
        fullList.value = LoadingState.Loading()
        fullList.value =
            try {
                val projetosAtividades = sofApi.getProjetoAtividade(codProjetoAtividade = null).lstProjetosAtividades
                LoadingState.Success(projetosAtividades)
            } catch (e: Exception) {
                e.printStackTrace()
                LoadingState.Failure("Erro ao carregar projeto/atividade: ${e::class.simpleName}")
            }
    }

    private suspend fun loadProjetoAtividadeComTotalDespesas(projetoAtividadeRowState: ProjetoAtividadeRowState) {
        if (projetoAtividadeRowState.stateProjeto.value !is LoadingState.Success) loadProjetoNome(projetoAtividadeRowState)

        loadTotalDespesas(projetoAtividadeRowState)
    }

    private suspend fun loadProjetoNome(projetoAtividadeRowState: ProjetoAtividadeRowState) {

        projetoAtividadeRowState.stateProjeto.value = LoadingState.Loading()
        projetoAtividadeRowState.stateProjeto.value = try {
            LoadingState.Loading<String>()
            val nome = sofApi
                .getProjetoAtividade(codProjetoAtividade = projetoAtividadeRowState.codigo)
                .lstProjetosAtividades[0]
            LoadingState.Success(nome)
        } catch (e: Exception) {
            e.printStackTrace()
            LoadingState.Failure("Erro ao carregar projeto/atividade: ${e::class.simpleName}")
        }
    }

    private suspend fun loadTotalDespesas(projetoAtividadeRowState: ProjetoAtividadeRowState) {

        try {
            projetoAtividadeRowState.stateTotalDespesas.value = LoadingState.Loading()

            val despesasResponse = sofApi
                .getDespesa(
                    codProjetoAtividade = projetoAtividadeRowState.codigo,
                    codOrgao = projetoAtividadeRowState.codOrgao
                )

            val totalDespesas = TotalDespesas.fromDespesasResponse(despesasResponse)

            projetoAtividadeRowState.stateTotalDespesas.value = LoadingState.Success(totalDespesas)
        } catch (e: Exception) {
            e.printStackTrace()
            projetoAtividadeRowState.stateTotalDespesas.value = LoadingState.Failure("Erro ao carregar despesas: ${e::class.simpleName}")
        }
    }


    private fun launchCoroutine(block: suspend  () -> Unit): Job =
        viewModelScope.launch(Dispatchers.Default) {
            block.invoke()
        }


    @Suppress("UNCHECKED_CAST")
    class Factory: ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
            return AppViewModel() as T
        }
    }

    companion object {

        var screenState: MutableState<Screen> = mutableStateOf(Screen.Home())

        fun navigateToHome() {
            screenState.value = Screen.Home()
        }

        fun navigateToFundurb() {
            screenState.value = Screen.Fundurb()
        }

        fun navigateToBuscaProjeto() {
            screenState.value = Screen.BuscaProjeto(::navigateToHome)
        }

        fun navigateToBuscaEmpenho() {
            screenState.value = Screen.BuscaEmpenho(::navigateToHome)
        }

        fun navigateToProjetoAtividade(projetoAtividade: ProjetoAtividade, navigateUp: (() -> Unit)?) {
            screenState.value = Screen.ProjetoAtividade(
                projetoAtividade,
                true,
                navigateUp ?: ::navigateToHome
            )
        }

        fun navigateToEmpenho(empenho: Empenho, navigateUp: (() -> Unit)?) {
            screenState.value = Screen.Empenho(
                empenho,
                true,
                navigateUp ?: ::navigateToHome
            )
        }

        fun navigateToGrupo(grupoState: GrupoState) {
            screenState.value = Screen.Grupo(
                grupoState,
                true,
                ::navigateToHome
            )
        }
    }

}