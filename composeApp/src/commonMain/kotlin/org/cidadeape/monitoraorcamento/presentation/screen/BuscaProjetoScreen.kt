package org.cidadeape.monitoraorcamento.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import org.cidadeape.monitoraorcamento.common.Util
import org.cidadeape.monitoraorcamento.common.colorizedText
import org.cidadeape.monitoraorcamento.data.model.projetosAtividades.ProjetoAtividade
import org.cidadeape.monitoraorcamento.presentation.AppViewModel
import org.jetbrains.compose.resources.imageResource

@Composable
fun BuscaProjetoScreen(
    appViewModel: AppViewModel
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        val fullListState = appViewModel.fullList.collectAsState()

        when (val state = fullListState.value) {
            is LoadingState.Success -> {
                ListLoadedView(appViewModel, state.response)
            }
            is LoadingState.Loading -> {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "Carregando..."
                )
            }
            else -> {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "Erro ao carregar lista de projetos e atividades"
                )
            }
        }
    }
}

@Composable
fun ListLoadedView(
    appViewModel: AppViewModel,
    fullList: List<ProjetoAtividade>
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {

        Text("Buscar projetos e atividades por código")

        var textCodigos by remember { mutableStateOf("") }

        TextField(
            modifier = Modifier.fillMaxWidth().padding(0.dp, 16.dp, 0.dp, 16.dp),
            value = textCodigos,
            onValueChange = { newValue ->
                textCodigos = newValue
            },
            placeholder = {
                Text(text = "Digite os códigos separados por vírgula")
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )

        Button(
            modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 32.dp),
            onClick = {
                val codigos = textCodigos.split(",").map { it.trim() }

                val projetos = fullList.filter { codigos.contains(it.codProjetoAtividade) }

                projetos.forEach {
                    appViewModel.adicionarAListaCustomizada(it)
                }
            }
        ) {
            Text("Carregar")
        }

        Text("Buscar projeto e atividade por termo")

        AutocompleteTextView(appViewModel, fullList)

        val lista = remember { appViewModel.listaCustomizadaProjetosAtividades }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            items(lista) { projetoAtividade ->
                ProjetoAtividadeRow(appViewModel, projetoAtividade)
                Spacer(
                    modifier = Modifier.fillMaxWidth().height(1.dp)
                        .background(AppColors.SuperLightGray)
                )
            }
        }
    }
}

@Composable
fun AutocompleteTextView(
    viewModel: AppViewModel,
    fullList: List<ProjetoAtividade>
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        var expanded by remember { mutableStateOf(false) }

        var inputText by remember { mutableStateOf("") }

        var filteredOptions = if (inputText.length < 3) {
            listOf()
        } else {
            fullList.filter {
                formatProjetoAtividade(it).contains(inputText, ignoreCase = true)
            }
        }

        TextField(
            modifier = Modifier.fillMaxWidth().padding(0.dp, 16.dp, 0.dp, 0.dp),
            value = inputText,
            onValueChange = { newValue ->
                inputText = newValue
                expanded = true
            },
            placeholder = {
                Text(text = "Digite um termo para filtrar")
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
                            viewModel.adicionarAListaCustomizada(option)
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

@Composable
fun ProjetoAtividadeRow(
    appViewModel: AppViewModel,
    projetoAtividadeRowState: ProjetoAtividadeRowState
) {

    val projetoState = projetoAtividadeRowState.stateProjeto.collectAsState()
    val totalEmpenhadoState = projetoAtividadeRowState.stateTotalDespesas.collectAsState()

    Row(
        modifier = Modifier.fillMaxWidth()
            .clickable {
                val state = projetoState.value as? LoadingState.Success<ProjetoAtividade>
                state?.let {
                    AppViewModel.navigateToProjetoAtividade(state.response, AppViewModel::navigateToHome)
                }
            }.padding(0.dp, 16.dp, 0.dp, 16.dp)
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
                text = "Codigo: ${projetoAtividadeRowState.codigo}"
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
                        colorizedText(text = Util.formatToCurrency(state.response.empenhadoLiquido), color = Color.Black)
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
                    .clickable { appViewModel.removerDaListaCustomizada(projetoAtividadeRowState) },
                bitmap = imageResource(Res.drawable.close_24dp),
                contentDescription = "Remover"
            )
        }
    }
}

private fun formatProjetoAtividade(projetoAtividade: ProjetoAtividade): String {
    return "${projetoAtividade.codProjetoAtividade} - ${projetoAtividade.txtDescricaoProjetoAtividade}"
}
