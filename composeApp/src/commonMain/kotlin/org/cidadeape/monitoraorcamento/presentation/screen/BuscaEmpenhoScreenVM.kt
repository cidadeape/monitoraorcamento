package org.cidadeape.monitoraorcamento.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.cidadeape.monitoraorcamento.common.LoadingState
import org.cidadeape.monitoraorcamento.common.Util
import org.cidadeape.monitoraorcamento.data.ApiSof
import org.cidadeape.monitoraorcamento.data.IApiSof
import org.cidadeape.monitoraorcamento.data.model.empenhos.Empenho

class BuscaEmpenhoScreenVM(
    private val apiSof: IApiSof = ApiSof()
): ViewModel() {

    var stateTotalEmpenhado: MutableStateFlow<LoadingState<String>> =
        MutableStateFlow(LoadingState.NotStarted())

    var stateListaEmpenhos: MutableStateFlow<LoadingState<List<Empenho>>> =
        MutableStateFlow(LoadingState.NotStarted())

    fun buscarEmpenhos(codOrgao: Int, codFonteRecursos: String) = viewModelScope.launch(Dispatchers.Default) {

        stateTotalEmpenhado.value = LoadingState.Loading()
        stateListaEmpenhos.value = LoadingState.Loading()

        val fonteRecursoElements = codFonteRecursos.split(".")
        val codFonte = fonteRecursoElements.getOrNull(0)
        val codReferencia = fonteRecursoElements.getOrNull(1)
        val codDestinacaoRecurso = fonteRecursoElements.getOrNull(2)
        val codVinculacaoRecurso = fonteRecursoElements.getOrNull(3)

        try {
            val empenhos = apiSof.getEmpenhos(
                codOrgao = codOrgao,
                codFonteRecurso = codFonte,
                codReferencia = codReferencia,
                codDestinacaoRecurso = codDestinacaoRecurso,
                codVinculacaoRecurso = codVinculacaoRecurso)
            stateListaEmpenhos.value = LoadingState.Success(empenhos)

            val totalEmpenhado = empenhos.sumOf { it.valEmpenhadoLiquido }
            stateTotalEmpenhado.value = LoadingState.Success(Util.formatToCurrency(totalEmpenhado))

        } catch (e: Exception) {
            e.printStackTrace()
            stateListaEmpenhos.value = LoadingState.Failure("Failed with exception: ${e.message}")
            stateTotalEmpenhado.value = LoadingState.Failure("Failed with exception: ${e.message}")
        }
    }

}