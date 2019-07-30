package io.codelabs.xhandieshub.viewmodel.repository

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import io.codelabs.xhandieshub.core.Callback
import io.codelabs.xhandieshub.core.Utils
import io.codelabs.xhandieshub.core.database.UserDao
import io.codelabs.xhandieshub.core.debugger
import io.codelabs.xhandieshub.core.prefs.AppPreferences
import io.codelabs.xhandieshub.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Responsible for making calls to the local & remote databases
 */
class UserRepository(
    private val auth: FirebaseAuth,
    private val userDao: UserDao,
    private val firestore: FirebaseFirestore,
    private val prefs: AppPreferences
) {

    private val ioScope = CoroutineScope(Dispatchers.IO)
//    private val uiScope = CoroutineScope(Dispatchers.Main)

    fun loginOrRegister(email: String, password: String, callback: Callback<User?>) {
        ioScope.launch {
            try {
                val firebaseUser =
                    Tasks.await(auth.signInWithEmailAndPassword(email, password)).user

                if (firebaseUser == null) {
                    debugger("User is null. Creating new instance")
                    val currentUser =
                        Tasks.await(auth.createUserWithEmailAndPassword(email, password)).user
                    val user = User(
                        currentUser.uid,
                        currentUser.email!!,
                        creditCard = Utils.DUMMY_CC,
                        cashBalance = 0
                    )

                    // Store in remote database
                    Tasks.await(
                        firestore.collection(Utils.USER_COLLECTION).document(user.uid).set(
                            user,
                            SetOptions.merge()
                        )
                    )

                    // Store in local database
                    userDao.insertItem(user)
                    prefs.uid = user.uid
                    callback(user)

                } else {
                    val user =
                        Tasks.await(
                            firestore.collection(Utils.USER_COLLECTION).document(
                                firebaseUser.uid
                            ).get()
                        )
                            .toObject(User::class.java)
                    if (user == null) {
                        debugger("User is null and cannot be saved")
                        callback(null)
                    } else {
                        // Store in local database
                        userDao.insertItem(user)
                        prefs.uid = user.uid
                        callback(user)
                    }
                }
            } catch (e: Exception) {
                debugger(e.localizedMessage)
//                if (e is FirebaseAuthInvalidUserException) {
                val currentUser =
                    Tasks.await(auth.createUserWithEmailAndPassword(email, password)).user
                val user = User(
                    currentUser.uid,
                    currentUser.email!!,
                    creditCard = Utils.DUMMY_CC,
                    cashBalance = 0
                )

                // Store in remote database
                Tasks.await(
                    firestore.collection(Utils.USER_COLLECTION).document(user.uid).set(
                        user,
                        SetOptions.merge()
                    )
                )

                // Store in local database
                userDao.insertItem(user)
                prefs.uid = user.uid
                callback(user)
//                } else {
//                    callback(null)
//                }
            }
        }
    }

    fun getCurrentUser(): LiveData<User> = userDao.getCurrentUser(prefs.uid)

    fun resetPassword(email: String, callback: Callback<String>) {
        try {
            Tasks.await(auth.sendPasswordResetEmail(email))
            callback("Email sent successfully")
        } catch (ex: Exception) {
            debugger(ex.localizedMessage)
            callback(ex.localizedMessage ?: "Cannot send reset mail")
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            auth: FirebaseAuth,
            userDao: UserDao,
            firestore: FirebaseFirestore,
            prefs: AppPreferences
        ): UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository(auth, userDao, firestore, prefs).also { instance = it }
        }
    }

}