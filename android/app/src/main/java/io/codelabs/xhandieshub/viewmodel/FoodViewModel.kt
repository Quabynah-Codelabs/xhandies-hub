package io.codelabs.xhandieshub.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import io.codelabs.xhandieshub.core.common.QueryLiveData
import io.codelabs.xhandieshub.model.Food
import io.codelabs.xhandieshub.viewmodel.repository.FoodRepository
import kotlinx.coroutines.launch

/**
 * [Food] [AndroidViewModel] subclass
 * Communicates with the underlying [FoodRepository]
 */
class FoodViewModel(app: Application, private val repository: FoodRepository) :
    AndroidViewModel(app) {

    fun getAllFoods(): QueryLiveData<Food> = repository.getAllFoods()

    fun getAllLocalFoods(): LiveData<MutableList<Food>> = repository.getAllLocalFoods()

    fun getFoodByKey(key: String): LiveData<Food> = repository.getFoodByKey(key)

    fun addToCart(food: Food) = viewModelScope.launch {
        repository.addToCart(food)
    }

    fun removeFromCart(food: Food) = viewModelScope.launch {
        repository.removeFromCart(food)
    }

    fun clearCart() = viewModelScope.launch {
        repository.clearCart()
    }

}