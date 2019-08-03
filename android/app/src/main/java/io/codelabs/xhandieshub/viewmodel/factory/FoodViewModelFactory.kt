package io.codelabs.xhandieshub.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.codelabs.xhandieshub.viewmodel.FoodViewModel
import io.codelabs.xhandieshub.viewmodel.repository.FoodRepository

/**
 * Factory for creating a new instance of the [FoodViewModel] class
 */
class FoodViewModelFactory(private val app: Application, private val repository: FoodRepository) :
    ViewModelProvider.AndroidViewModelFactory(app) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FoodViewModel(app, repository) as T
    }

}