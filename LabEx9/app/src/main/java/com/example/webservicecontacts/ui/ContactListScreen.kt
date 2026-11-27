package com.example.webservicecontacts.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.webservicecontacts.model.User
import com.example.webservicecontacts.network.RetrofitInstance

sealed interface UsersUiState {
    data object Loading : UsersUiState
    data class Success(val users: List<User>) : UsersUiState
    data class Error(val message: String) : UsersUiState
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactListScreen(onContactClick: (Int) -> Unit) {
    var uiState by remember { mutableStateOf<UsersUiState>(UsersUiState.Loading) }

    LaunchedEffect(Unit) {
        uiState = try {
            UsersUiState.Success(RetrofitInstance.api.getUsers())
        } catch (e: Exception) {
            UsersUiState.Error("Could not load contacts. Check your internet connection.")
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Contacts") }) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is UsersUiState.Loading -> CircularProgressIndicator()

                is UsersUiState.Error -> Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )

                is UsersUiState.Success -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.users, key = { it.id }) { user ->
                        ContactRow(user = user, onClick = { onContactClick(user.id) })
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
fun ContactRow(user: User, onClick: () -> Unit) {
    ListItem(
        headlineContent = {
            Text(text = "☘ ${user.name}", fontWeight = FontWeight.SemiBold)
        },
        supportingContent = { Text(user.email) },
        modifier = Modifier.clickable(onClick = onClick)
    )
}
