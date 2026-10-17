package com.example.sqlitecompose.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.sqlitecompose.data.Customer
import com.example.sqlitecompose.data.CustomerDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CustomerViewModel(private val customerDao: CustomerDao) : ViewModel() {

    val customers: Flow<List<Customer>> = customerDao.getAllCustomers()

    fun addCustomer(name: String, age: Int, isActive: Boolean) {
        viewModelScope.launch {
            customerDao.insert(Customer(name = name, age = age, isActive = isActive))
        }
    }

    fun deleteCustomer(customer: Customer) {
        viewModelScope.launch {
            customerDao.delete(customer)
        }
    }
}

class CustomerViewModelFactory(private val customerDao: CustomerDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CustomerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CustomerViewModel(customerDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}