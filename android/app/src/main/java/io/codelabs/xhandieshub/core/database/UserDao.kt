package io.codelabs.xhandieshub.core.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import io.codelabs.xhandieshub.model.User

@Dao
interface UserDao : BaseDao<User> {

    @Query("SELECT * FROM users WHERE uid = :uid")
    fun getCurrentUser(uid: String?): LiveData<User>

}