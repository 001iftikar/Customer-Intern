package com.iftikar.customerintern

import android.app.Application
import com.descope.Descope
import com.iftikar.customerintern.utils.DESCOPE_PROJECT_ID
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TravenorApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Descope.setup(this, projectId = DESCOPE_PROJECT_ID)
    }
}