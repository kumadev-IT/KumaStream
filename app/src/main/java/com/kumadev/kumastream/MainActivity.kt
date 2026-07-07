package com.kumadev.kumastream

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.kumadev.kumastream.ui.navigation.KumaNavHost
import com.kumadev.kumastream.ui.theme.KumaStreamTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Single-Activity host for the Compose UI. Owns the theme and the navigation
 * graph; every screen lives inside [KumaNavHost].
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            KumaStreamTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    KumaNavHost()
                }
            }
        }
    }
}
