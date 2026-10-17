package com.example.sqlitecompose.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import com.example.sqlitecompose.data.Customer

@Composable
fun CustomerScreen(onAdd: (String, Int, Boolean) -> Unit) {
    var name by rememberSaveable { mutableStateOf("") }
    var age by rememberSaveable { mutableStateOf("") }
    var isActive by rememberSaveable { mutableStateOf(true) }
    var message by rememberSaveable { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = age,
            onValueChange = { if (it.all { c -> c.isDigit() }) age = it },
            label = { Text("Age") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Active")
            Spacer(modifier = Modifier.height(0.dp))
            Switch(checked = isActive, onCheckedChange = { isActive = it })
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            val ageInt = age.toIntOrNull()
            if (name.isBlank() || ageInt == null) {
                message = "Enter a valid name and age."
            } else {
                onAdd(name.trim(), ageInt, isActive)
                name = ""
                age = ""
                message = "Customer submitted."
            }
        }) {
            Text("Add Customer")
        }
        if (message.isNotEmpty()) {
            Text(message, modifier = Modifier.padding(top = 4.dp))
        }
    }
}

@Composable
fun CustomerList(
    customers: List<Customer>,
    onDelete: (Customer) -> Unit,
    modifier: Modifier = Modifier
) {
    if (customers.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No customers in the database.")
        }
    } else {
        LazyColumn(modifier = modifier) {
            items(customers, key = { it.id }) { customer ->
                CustomerRow(customer = customer, onDelete = onDelete)
            }
        }
    }
}

@Composable
fun CustomerRow(customer: Customer, onDelete: (Customer) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(customer.name, style = MaterialTheme.typography.titleMedium)
                Text("ID: ${customer.id}  •  Age: ${customer.age}")
                Text(if (customer.isActive) "Active" else "Inactive")
            }
            TextButton(onClick = { onDelete(customer) }) {
                Text("Delete")
            }
        }
    }
}