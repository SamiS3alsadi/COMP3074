package com.example.webservicecontacts.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.webservicecontacts.model.User
import com.example.webservicecontacts.network.RetrofitInstance

sealed interface UserUiState {
    data object Loading : UserUiState
    data class Success(val user: User) : UserUiState
    data class Error(val message: String) : UserUiState
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDetailsScreen(userId: Int?, onBackClick: () -> Unit) {
    var uiState by remember { mutableStateOf<UserUiState>(UserUiState.Loading) }

    LaunchedEffect(userId) {
        uiState = if (userId == null) {
            UserUiState.Error("Invalid contact id.")
        } else {
            try {
                UserUiState.Success(RetrofitInstance.api.getUser(userId))
            } catch (e: Exception) {
                UserUiState.Error("Could not load contact. Check your internet connection.")
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contact Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is UserUiState.Loading -> CircularProgressIndicator()

                is UserUiState.Error -> Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )

                is UserUiState.Success -> ContactDetails(user = state.user)
            }
        }
    }
}

@Composable
fun ContactDetails(user: User) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(text = user.name, style = MaterialTheme.typography.headlineSmall)

        DetailRow(label = "ID", value = user.id.toString())
        DetailRow(label = "Username", value = user.username)
        DetailRow(label = "Email", value = user.email)
        DetailRow(
            label = "Phone",
            value = user.phone,
            onClick = { dialPhoneNumber(context, user.phone) }
        )
        DetailRow(label = "Website", value = user.website)

        SectionTitle("Address")
        DetailRow(label = "Street", value = user.address.street)
        DetailRow(label = "Suite", value = user.address.suite)
        DetailRow(label = "City", value = user.address.city)
        DetailRow(label = "Zip", value = user.address.zipcode)
        DetailRow(label = "Lat", value = user.address.geo.lat)
        DetailRow(label = "Lng", value = user.address.geo.lng)

        SectionTitle("Company")
        DetailRow(label = "Name", value = user.company.name)
        DetailRow(label = "Catch phrase", value = user.company.catchPhrase)
        DetailRow(label = "BS", value = user.company.bs)
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 24.dp)
    )
}

/** A label with its value; when [onClick] is given the value is shown as a link. */
@Composable
fun DetailRow(label: String, value: String, onClick: (() -> Unit)? = null) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold
        )
        if (onClick != null) {
            Text(
                text = value,
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline
            )
        } else {
            Text(text = value)
        }
    }
}

// ACTION_DIAL only opens the dialer with the number filled in; the user still has to
// press call, so no CALL_PHONE permission is needed (unlike ACTION_CALL).
private fun dialPhoneNumber(context: Context, phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        // No dialer app on this device (e.g. some tablets and emulators): do nothing.
    }
}
