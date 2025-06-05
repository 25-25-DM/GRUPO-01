
@file:OptIn(ExperimentalMaterial3Api::class)

package uce.edu.ec.ui
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import uce.edu.ec.R
import uce.edu.ec.model.MarsPhoto
import uce.edu.ec.ui.screens.HomeScreen
import uce.edu.ec.ui.screens.MarsViewModel

import androidx.navigation.NavController

@Composable
fun MarsPhotosApp(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val marsViewModel: MarsViewModel = viewModel()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MarsTopAppBar(
                scrollBehavior = scrollBehavior,
                onNavigateToCount = { navController.navigate("count") }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            HomeScreen(
                marsUiState = marsViewModel.marsUiState,
                contentPadding = innerPadding
            )
        }
    }
}


@Composable
fun MarsTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    onNavigateToCount: () -> Unit
) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineSmall,
            )
        },
        actions = {
            Button(
                onClick = onNavigateToCount,
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Text("Ver Total JSON", color = Color.White)
            }
        },
        modifier = modifier
    )
}


