package org.cidadeape.monitoraorcamento.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import monitoraorcamento.composeapp.generated.resources.Res
import monitoraorcamento.composeapp.generated.resources.close_24dp
import org.cidadeape.monitoraorcamento.common.AppColors
import org.cidadeape.monitoraorcamento.common.LoadingState
import org.cidadeape.monitoraorcamento.common.colorizedText
import org.cidadeape.monitoraorcamento.data.model.projetosAtividades.ProjetoAtividade
import org.cidadeape.monitoraorcamento.presentation.AppViewModel
import org.jetbrains.compose.resources.imageResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    appViewModel: AppViewModel,
    homeViewModel: HomeViewModel
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Text(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = AppColors.Purple,
            text = "VALORES EMPENHADOS EM 2025"
        )

        AutocompleteTextView(homeViewModel)

        val isRefreshing by remember { homeViewModel.refreshingState }

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                homeViewModel.load()
            }
        ) {

            val lista = remember { homeViewModel.listaProjetosAtividades }

            LazyColumn (
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                items(lista) { projetoAtividade ->
                    ProjetoAtividadeRow(appViewModel, homeViewModel, projetoAtividade)
                    Spacer(modifier = Modifier.fillMaxWidth().height(1.dp).background(AppColors.SuperLightGray))
                }
            }
        }
    }
}

@Composable
fun ProjetoAtividadeRow(
    appViewModel: AppViewModel,
    viewModel: HomeViewModel,
    projetoAtividadeState: HomeViewModel.ProjetoAtividadeState
) {

    val projetoState = projetoAtividadeState.stateProjeto.collectAsState()
    val totalEmpenhadoState = projetoAtividadeState.stateTotalEmpenhado.collectAsState()

    Row(
        modifier = Modifier.fillMaxWidth()
            .clickable {
                val state = projetoState.value as? LoadingState.Success<ProjetoAtividade>
                state?.let {
                    appViewModel.navigateToProjetoAtividade(state.response)
                }
            }.padding(16.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {

            Text(
                text = when (val state = projetoState.value) {
                    is LoadingState.Loading -> {
                        colorizedText(text = "Carregando...", color = Color.Black)
                    }
                    is LoadingState.Success -> {
                        colorizedText(
                            text = state.response.txtDescricaoProjetoAtividade,
                            color = Color.Black
                        )
                    }
                    is LoadingState.Failure -> {
                        colorizedText(text = state.message, color = Color.Red)
                    }
                    else -> colorizedText(text = "-", color = Color.Black)
                }
            )

            Text (
                fontSize = 12.sp,
                text = "Codigo: ${projetoAtividadeState.codigo}"
            )

            Text(
                modifier = Modifier.fillMaxWidth().padding(0.dp, 0.dp, 0.dp, 0.dp),
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                text = when (val state = totalEmpenhadoState.value) {
                        is LoadingState.Loading -> {
                            colorizedText(text = "Carregando...", color = Color.Black)
                        }
                        is LoadingState.Success -> {
                            colorizedText(text = state.response, color = Color.Black)
                        }
                        is LoadingState.Failure -> {
                            colorizedText(text = state.message, color = Color.Red)
                        }
                        else -> colorizedText(text = "-", color = Color.Black)
                    }
            )
        }
        Column(modifier = Modifier
            .wrapContentWidth()
            .align(Alignment.CenterVertically)
        ) {

            Image(
                modifier = Modifier
                    .padding(8.dp)
                    .size(24.dp)
                    .clickable {
                        viewModel.removeFromList(projetoAtividadeState)
                               },
                bitmap = imageResource(Res.drawable.close_24dp),
                contentDescription = "Remover"
            )
        }
    }
}


@Composable
fun AutocompleteTextView(
    viewModel: HomeViewModel
) {

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        val fullListState = viewModel.fullList.collectAsState()

        var expanded by remember { mutableStateOf(false) }

        var inputText by remember { mutableStateOf("") }

        val state = fullListState.value

        var filteredOptions = if (inputText.length < 3 || state !is LoadingState.Success) {
            listOf()
        } else {
            state.response.filter {
                formatProjetoAtividade(it).contains(inputText, ignoreCase = true)
            }
        }

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = inputText,
            onValueChange = { newValue ->
                inputText = newValue
                expanded = true
            },
            placeholder = {
                Text(text = "Buscar projeto / atividade")
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )

        if (filteredOptions.isNotEmpty() && expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .background(Color.Gray)
            ) {
                for (option in filteredOptions) {
                    DropdownMenuItem(
                        modifier = Modifier
                            .padding(0.dp, 0.dp, 0.dp, 1.dp)
                            .background(Color.LightGray)
                            .padding(4.dp),
                        onClick = {
                            filteredOptions = listOf()
                            inputText = ""
                            expanded = false
                            viewModel.addToList(option)
                        },
                        text = {
                            Text(text = formatProjetoAtividade(option))
                        }
                    )
                }
            }
        }
    }
}

private fun formatProjetoAtividade(projetoAtividade: ProjetoAtividade): String {
    return "${projetoAtividade.codProjetoAtividade} - ${projetoAtividade.txtDescricaoProjetoAtividade}"
}
