package io.codelabs.xhandieshub.core

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import io.codelabs.sdk.view.BaseActivity
import io.codelabs.xhandieshub.core.datasource.Preferences
import io.codelabs.xhandieshub.core.datasource.room.HubRoomDao
import org.koin.android.ext.android.inject

abstract class HubBaseActivity : BaseActivity() {
    val auth: FirebaseAuth by inject()
    val firestore: FirebaseFirestore by inject()
    val ref: StorageReference by inject()
    val prefs: Preferences by inject()
    val dao: HubRoomDao by inject()
}