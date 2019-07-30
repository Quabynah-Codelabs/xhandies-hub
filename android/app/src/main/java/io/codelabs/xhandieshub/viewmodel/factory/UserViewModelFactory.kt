package io.codelabs.xhandieshub.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.codelabs.xhandieshub.viewmodel.UserViewModel
import io.codelabs.xhandieshub.viewmodel.repository.UserRepository

/**
 * Factory for creating a new instance of the [UserViewModel] class
 */
class UserViewModelFactory(private val app: Application, private val repository: UserRepository) :
    ViewModelProvider.AndroidViewModelFactory(app) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserViewModel(app, repository) as T
    }

}