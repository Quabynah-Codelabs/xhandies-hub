package io.codelabs.xhandieshub.core.injection

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import io.codelabs.xhandieshub.BuildConfig
import io.codelabs.xhandieshub.core.database.AppDatabase
import io.codelabs.xhandieshub.core.prefs.AppPreferences
import io.codelabs.xhandieshub.viewmodel.FoodViewModel
import io.codelabs.xhandieshub.viewmodel.UserViewModel
import io.codelabs.xhandieshub.viewmodel.factory.FoodViewModelFactory
import io.codelabs.xhandieshub.viewmodel.factory.UserViewModelFactory
import io.codelabs.xhandieshub.viewmodel.repository.FoodRepository
import io.codelabs.xhandieshub.viewmodel.repository.UserRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Inject Firebase module
 */
val firebaseModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FirebaseMessaging.getInstance() }
    single {
        FirebaseStorage.getInstance().reference.child(
            BuildConfig.APPLICATION_ID.replace(
                ".",
                ""
            )
        )
    }
}

/**
 * Inject Room module
 */
val roomModule = module {
    single { AppDatabase.getInstance(androidContext()) }
    single { get<AppDatabase>().userDao() }
    single { get<AppDatabase>().foodDao() }

    /*Repositories*/
    single { UserRepository.getInstance(get(), get(), get(), get()) }
    single { FoodRepository.getInstance(get(), get()) }

    /*Factories*/
    single { UserViewModelFactory(androidApplication(), get()) }
    single { FoodViewModelFactory(androidApplication(), get()) }

    /*ViewModel*/
    viewModel { UserViewModel(androidApplication(), get()) }
    viewModel { FoodViewModel(androidApplication(), get()) }
}

/**
 * Inject prefs module
 */
val prefsModule = module {
    single { AppPreferences.getInstance(androidContext()) }
}