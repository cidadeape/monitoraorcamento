package org.cidadeape.monitoraorcamento.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.cidadeape.monitoraorcamento.common.CategoriaDespesa
import org.cidadeape.monitoraorcamento.common.LoadingState
import org.cidadeape.monitoraorcamento.common.Util
import org.cidadeape.monitoraorcamento.data.ApiSof
import org.cidadeape.monitoraorcamento.data.IApiSof
import org.cidadeape.monitoraorcamento.data.model.empenhos.Empenho
import org.cidadeape.monitoraorcamento.data.model.projetosAtividades.ProjetoAtividade
import org.cidadeape.monitoraorcamento.presentation.grupo.GrupoState
import org.cidadeape.monitoraorcamento.presentation.grupo.ProjetoAtividadeState
import org.cidadeape.monitoraorcamento.presentation.grupo.TotalEmpenhos
import kotlin.reflect.KClass

class AppViewModel(
    private val sofApi: IApiSof = ApiSof()
): ViewModel() {

    var screenState: MutableState<Screen> = mutableStateOf(Screen.Home())

    private val grupoAsfalto = GrupoState(
        nome = "Asfalto",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeState("1137"),
            ProjetoAtividadeState("2340")
        ))

    private val grupoCalcadas = GrupoState(
        nome = "Calçadas",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeState("1169")
        ))

    private val grupoBicicleta = GrupoState(
        nome = "Ciclovias e Ciclofaixas",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeState("1097"),
            ProjetoAtividadeState("1098"),
            ProjetoAtividadeState("2093"),
            ProjetoAtividadeState("2098")
        ))

    private val grupoSegurancaViaria = GrupoState(
        nome = "Segurança Viária",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeState("3757")
        ))

    private val grupoOnibusCorredores = GrupoState(
        nome = "Ônibus - Corredores e Faixas",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeState("1099"),
            ProjetoAtividadeState("1100"),
            ProjetoAtividadeState("2099"),
            ProjetoAtividadeState("5391"),
            ProjetoAtividadeState("5392"),
            ProjetoAtividadeState("5393"),
        ))

    private val grupoOnibusTerminais = GrupoState(
        nome = "Ônibus - Terminais",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeState("1095"),
            ProjetoAtividadeState("1096"),
            ProjetoAtividadeState("2096"),
            ProjetoAtividadeState("3749"),
            ProjetoAtividadeState("4663"),
        ))

    private val grupoOnibusFrota = GrupoState(
        nome = "Ônibus - Frota",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeState("1800"),
            ProjetoAtividadeState("3800"),
            ProjetoAtividadeState("3801")
        ))

    private val grupoOnibusCompensacoes = GrupoState(
        nome = "Ônibus - Compensação tarifária",
        listaProjetosAtividades = listOf(
            ProjetoAtividadeState("4701")
        ))

    val listaGrupos = listOf(
        grupoAsfalto,
        grupoCalcadas,
        grupoBicicleta,
        grupoSegurancaViaria,
        grupoOnibusCorredores,
        grupoOnibusTerminais,
        grupoOnibusFrota,
        grupoOnibusCompensacoes
    )

    val listaProjetosAtividades = mutableStateListOf<ProjetoAtividadeState>()

    val fullList: MutableStateFlow<LoadingState<List<ProjetoAtividade>>> = MutableStateFlow(
        LoadingState.NotStarted())

    val refreshingState = mutableStateOf(false)

    init {
        load()
    }

    fun load() = launchCoroutine {
        refreshingState.value = true
        loadAllGrupos()
        loadSearchList()
        refreshingState.value = false
    }

    private fun loadAllGrupos() = launchCoroutine {
        for (grupo in listaGrupos) {
            loadGrupo(grupo)
        }
    }

    fun loadGrupo(grupoState: GrupoState) = launchCoroutine {
        grupoState.refreshing.value = true
        grupoState.stateTotalEmpenhadoGrupo.value = LoadingState.Loading()
        for (projeto in grupoState.listaProjetosAtividades) {
            loadProjetoAtividadeComTotalEmpenhado(projeto)
        }

        val totalEmpenho = TotalEmpenhos(0.0, 0.0, 0.0, 0.0)
        val failedList = grupoState.listaProjetosAtividades.filter {

            when (val state = it.stateTotalEmpenhado.value) {
                is LoadingState.Success -> {
                    totalEmpenho.total += state.response.total
                    totalEmpenho.despCorrentes += state.response.despCorrentes
                    totalEmpenho.despCapital += state.response.despCapital
                    totalEmpenho.resContingencia += state.response.resContingencia
                    false
                }
                else -> {
                    true
                }
            }
        }

        if (failedList.isNotEmpty()) {
            grupoState.stateTotalEmpenhadoGrupo.value = LoadingState.Failure("Erro ao carregar totais")
        } else {
            grupoState.stateTotalEmpenhadoGrupo.value = LoadingState.Success(totalEmpenho)
        }

        grupoState.refreshing.value = false
    }

    fun removeFromList(projetoAtividadeState: ProjetoAtividadeState) {

        listaProjetosAtividades.remove(projetoAtividadeState)
    }

    fun addToList(projetoAtividade: ProjetoAtividade) {

        val projetoAtividadeState = ProjetoAtividadeState(
            codigo = projetoAtividade.codProjetoAtividade,
            stateProjeto = MutableStateFlow(LoadingState.Success(projetoAtividade))
        )

        if (listaProjetosAtividades.contains(projetoAtividadeState)) return

        listaProjetosAtividades.add(0, projetoAtividadeState)
        launchCoroutine {
            loadProjetoAtividadeComTotalEmpenhado(projetoAtividadeState)
        }
    }

    private suspend fun loadSearchList() {
        fullList.value = LoadingState.Loading()
        fullList.value =
            try {
                val projetosAtividades = sofApi.getProjetoAtividade("2025", null).lstProjetosAtividades
                LoadingState.Success(projetosAtividades)
            } catch (e: Exception) {
                e.printStackTrace()
                LoadingState.Failure("Erro ao carregar projeto/atividade: ${e::class.simpleName}")
            }
    }

    private suspend fun loadProjetoAtividadeComTotalEmpenhado(projetoAtividadeState: ProjetoAtividadeState) {
        if (projetoAtividadeState.stateProjeto.value !is LoadingState.Success) loadProjetoNome(projetoAtividadeState)

        loadTotalEmpenhos(projetoAtividadeState)
    }

    private suspend fun loadProjetoNome(projetoAtividadeState: ProjetoAtividadeState) {

        projetoAtividadeState.stateProjeto.value = LoadingState.Loading()
        projetoAtividadeState.stateProjeto.value = try {
            LoadingState.Loading<String>()
            val nome = sofApi
                .getProjetoAtividade("2025", projetoAtividadeState.codigo)
                .lstProjetosAtividades[0]
            LoadingState.Success(nome)
        } catch (e: Exception) {
            e.printStackTrace()
            LoadingState.Failure("Erro ao carregar projeto/atividade: ${e::class.simpleName}")
        }
    }

    private suspend fun loadTotalEmpenhos(projetoAtividadeState: ProjetoAtividadeState) {

        projetoAtividadeState.stateTotalEmpenhado.value = LoadingState.Loading()

        projetoAtividadeState.stateTotalEmpenhado.value = try {
            val listaEmpenhos = sofApi
                .getEmpenhos("2025", "12", projetoAtividadeState.codigo)
                .lstEmpenhos

            val totalEmpenhado = listaEmpenhos
                .sumOf { it.valEmpenhadoLiquido }

            val despCorrentes = listaEmpenhos
                .filter { it.codCategoria == CategoriaDespesa.DESP_CORRENTES }
                .sumOf { it.valEmpenhadoLiquido }

            val despCapital = listaEmpenhos
                .filter { it.codCategoria == CategoriaDespesa.DESP_CAPITAL }
                .sumOf { it.valEmpenhadoLiquido }

            val resContingencia = listaEmpenhos
                .filter { it.codCategoria == CategoriaDespesa.RES_CONTINGENCIA }
                .sumOf { it.valEmpenhadoLiquido }

            val empenhos = TotalEmpenhos(
                totalEmpenhado,
                despCorrentes,
                despCapital,
                resContingencia
            )

            LoadingState.Success(empenhos)
        } catch (e: Exception) {
            e.printStackTrace()
            LoadingState.Failure("Erro ao carregar empenhos: ${e::class.simpleName}")
        }
    }


    private fun launchCoroutine(block: suspend  () -> Unit) =
        viewModelScope.launch(Dispatchers.Default) {
            block.invoke()
        }

    fun navigateToHome() {
        screenState.value = Screen.Home()
    }

    fun navigateToBusca() {
        screenState.value = Screen.Busca(::navigateToHome)
    }

    fun navigateToProjetoAtividade(projetoAtividade: ProjetoAtividade, navigateUp: (() -> Unit)?) {
        screenState.value = Screen.ProjetoAtividade(
            projetoAtividade,
            true,
            navigateUp ?: ::navigateToHome
        )
    }

    fun navigateToEmpenho(projetoAtividade: ProjetoAtividade, empenho: Empenho) {
        screenState.value = Screen.Empenho(
            empenho,
            true,
            { navigateToProjetoAtividade(projetoAtividade, null) }
        )
    }

    fun navigateToGrupo(grupoState: GrupoState) {
        screenState.value = Screen.Grupo(
            grupoState,
            true,
            ::navigateToHome
        )
    }

    @Suppress("UNCHECKED_CAST")
    class Factory: ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T {
            return AppViewModel() as T
        }
    }

}