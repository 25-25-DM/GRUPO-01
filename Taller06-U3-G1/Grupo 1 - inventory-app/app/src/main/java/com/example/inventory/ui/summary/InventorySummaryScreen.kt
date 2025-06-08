package com.example.inventory.ui.summary

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.InventoryTopAppBar
import com.example.inventory.R
import com.example.inventory.ui.AppViewModelProvider
import com.example.inventory.ui.home.HomeViewModel
import com.example.inventory.ui.navigation.NavigationDestination

object InventorySummaryDestination : NavigationDestination {
    override val route = "inventory_summary"
    override val titleRes = R.string.inventory_summary_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventorySummaryScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            InventoryTopAppBar(
                title = stringResource(R.string.inventory_summary_title),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(innerPadding),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Resumen del Inventario",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Total de items
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Total de items:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "${homeUiState.itemList.size}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                
                Divider()
                
                // Valor total del inventario
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Valor total:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "$${homeUiState.itemList.sumOf { it.price.toDouble() }}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                
                Divider()
                
                // Cantidad total en stock
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Cantidad total en stock:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "${homeUiState.itemList.sumOf { it.quantity }}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
} 