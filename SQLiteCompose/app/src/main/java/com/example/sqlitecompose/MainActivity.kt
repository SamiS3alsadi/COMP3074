package com.example.sqlitecompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sqlitecompose.data.AppDatabase
import com.example.sqlitecompose.ui.CustomerList
import com.example.sqlitecompose.ui.CustomerScreen
import com.example.sqlitecompose.ui.CustomerViewModel
import com.example.sqlitecompose.ui.CustomerViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = AppDatabase.getDatabase(this)
        val factory = CustomerViewModelFactory(database.customerDao())

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val viewModel: CustomerViewModel = viewModel(factory = factory)
                    CustomerApp(viewModel)
                }
            }
        }
    }
}

@Composable
fun CustomerApp(viewModel: CustomerViewModel) {
    val customers by viewModel.customers.collectAsStateWithLifecycle(initialValue = emptyList())

    Column(modifier = Modifier.fillMaxSize().statusBarsPadding().padding(16.dp)) {
        CustomerScreen(onAdd = viewModel::addCustomer)
        Spacer(modifier = Modifier.height(12.dp))
        CustomerList(
            customers = customers,
            onDelete = viewModel::deleteCustomer,
            modifier = Modifier.weight(1f)
        )
    }
}