package io.codelabs.xhandieshub.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import io.codelabs.xhandieshub.core.Callback
import io.codelabs.xhandieshub.model.User
import io.codelabs.xhandieshub.viewmodel.repository.UserRepository

class UserViewModel(private val app: Application, private val repository: UserRepository) :
    AndroidViewModel(app) {

    /**
     * Currently logged in user or null
     */
    val currentUser = repository.getCurrentUser()

    fun loginUser(email: String, password: String, callback: Callback<User?>) =
        repository.loginUser(email, password, callback)

    fun createAccount(username: String, email: String, password: String, callback: Callback<User?>) =
        repository.createAccount(username,email, password, callback)

    fun resetPassword(email: String, callback: Callback<String>) = repository.resetPassword(email, callback)

    fun updateUser(user: User?) = repository.updateUser(user)
}