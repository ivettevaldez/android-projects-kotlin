package com.ivettevaldez.saturnus.common

import android.app.Application
import com.ivettevaldez.saturnus.common.dependencyinjection.application.ApplicationComponent
import com.ivettevaldez.saturnus.common.dependencyinjection.application.ApplicationModule
import com.ivettevaldez.saturnus.common.dependencyinjection.application.DaggerApplicationComponent
import io.realm.Realm
import io.realm.RealmConfiguration

private const val DB_SCHEMA_VERSION = 1L // Must be bumped when the schema changes
private const val DB_NAME = "saturnus.realm" // DON'T UPDATE THIS WHEN SCHEMA CHANGES

class CustomApplication : Application() {

    val applicationComponent: ApplicationComponent by lazy {
        DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        initRealmDb()
    }

    private fun initRealmDb() {
        Realm.init(this)

        val config = RealmConfiguration.Builder()
            .name(DB_NAME)
            .schemaVersion(DB_SCHEMA_VERSION)
            .allowWritesOnUiThread(true)
//            .migration(DbMigration()) // Migration to run instead of throwing an exception
            .build()

        Realm.setDefaultConfiguration(config)
    }
}