package com.buyit.buyit.home.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.buyit.buyit.home.repositories.HomeRepository

@Suppress("UNCHECKED_CAST")
class HomeViewModelFactory(private val repository: HomeRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }
}