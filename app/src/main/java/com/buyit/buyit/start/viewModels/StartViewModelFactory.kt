package com.buyit.buyit.start.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.buyit.buyit.start.repositories.StartRepository

class StartViewModelFactory(private val repository: StartRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StartViewModel(repository) as T
    }
}