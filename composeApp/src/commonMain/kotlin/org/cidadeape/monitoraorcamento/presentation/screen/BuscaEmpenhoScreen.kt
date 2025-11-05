package org.cidadeape.monitoraorcamento.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.cidadeape.monitoraorcamento.common.TextTitle
import org.cidadeape.monitoraorcamento.presentation.AppViewModel
import org.cidadeape.monitoraorcamento.presentation.compose.ListaEmpenhos

@Composable
fun BuscaEmpenhoScreen(
    viewModel: BuscaEmpenhoScreenVM = BuscaEmpenhoScreenVM()
) {

    Column(
        modifier = Modifier.fillMaxSize().padding(0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TextTitle(
            text = "Busca de empenhos",
            modifier = Modifier.padding(16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            var textCodOrgao by remember { mutableStateOf("") }
            var textCodFonteRecursos by remember { mutableStateOf("") }

            TextField(
                modifier = Modifier.fillMaxWidth(.33f,).padding(0.dp, 16.dp, 0.dp, 16.dp),
                value = textCodOrgao,
                onValueChange = { newValue ->
                    textCodOrgao = newValue
                },
                placeholder = {
                    Text(text = "Código do órgão")
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )

            TextField(
                modifier = Modifier.fillMaxWidth(.5f).padding(0.dp, 16.dp, 0.dp, 16.dp),
                value = textCodFonteRecursos,
                onValueChange = { newValue ->
                    textCodFonteRecursos = newValue
                },
                placeholder = {
                    Text(text = "Código da Fonte de Recurso (ex: 08.1.759.0402)")
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )

            Button(
                modifier = Modifier,
                onClick = {
                    try {
                        val codOrgao = textCodOrgao.toInt()
                        viewModel.buscarEmpenhos(codOrgao, textCodFonteRecursos)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            ) {
                Text("Buscar")
            }
        }

        Column (modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 0.dp)) {
            ListaEmpenhos(viewModel.stateTotalEmpenhado, viewModel.stateListaEmpenhos) {
                AppViewModel.navigateToHome()
            }
        }
    }
}

@Composable
fun CamposBusca() {

}
