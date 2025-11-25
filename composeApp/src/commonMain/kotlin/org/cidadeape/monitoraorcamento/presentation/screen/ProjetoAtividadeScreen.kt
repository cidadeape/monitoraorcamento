package org.cidadeape.monitoraorcamento.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.cidadeape.monitoraorcamento.common.TextTitle
import org.cidadeape.monitoraorcamento.data.model.projetosAtividades.ProjetoAtividade
import org.cidadeape.monitoraorcamento.presentation.AppViewModel
import org.cidadeape.monitoraorcamento.presentation.compose.ListaEmpenhos

@Composable
fun ProjetoAtividadeScreen(
    viewModel: ProjetoAtividadeViewModel,
    projetoAtividade: ProjetoAtividade
) {

    viewModel.load(projetoAtividade)
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp)
        ) {

            TextTitle(text = viewModel.projetoAtividadeState.nome.value ?: "-")

            Text(
                fontSize = 12.sp,
                fontStyle = FontStyle.Italic,
                text = "Código: ${viewModel.projetoAtividadeState.codigo.value ?: "-"}"
            )
        }

        Column (modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 0.dp)) {
            ListaEmpenhos(
                viewModel.projetoAtividadeState.stateTotalEmpenhado,
                viewModel.projetoAtividadeState.stateListaEmpenhos,
                listName = "Empenhos projAtiv ${projetoAtividade.codProjetoAtividade}",
                { AppViewModel.navigateToProjetoAtividade(projetoAtividade, null) }
            )
        }
    }
}
