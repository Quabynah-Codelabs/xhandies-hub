package io.codelabs.xhandieshub.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import io.codelabs.xhandieshub.core.common.QueryLiveData
import io.codelabs.xhandieshub.model.Food
import io.codelabs.xhandieshub.viewmodel.repository.FoodRepository

/**
 * [Food] [AndroidViewModel] subclass
 * Communicates with the underlying [FoodRepository]
 */
class FoodViewModel(app: Application, private val repository: FoodRepository) :
    AndroidViewModel(app) {

    fun getAllFoods(): QueryLiveData<Food> = repository.getAllFoods()

    fun getAllLocalFoods(): LiveData<MutableList<Food>> = repository.getAllLocalFoods()

    fun getFoodByKey(key: String): LiveData<Food> = repository.getFoodByKey(key)

}