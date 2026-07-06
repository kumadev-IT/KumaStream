package com.kumadev.kumastream

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application entry point. Annotated with [HiltAndroidApp] so Hilt can generate
 * the app-level dependency container. Real DI modules are added during
 * implementation (see DatabaseModule, RepositoryModule, ApiModule).
 */
@HiltAndroidApp
class KumaStreamApplication : Application()
