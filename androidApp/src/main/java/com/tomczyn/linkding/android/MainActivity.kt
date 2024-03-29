package com.tomczyn.linkding.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.tomczyn.linkding.Settings
import com.tomczyn.linkding.android.ui.common.LinkdingNavHost
import com.tomczyn.linkding.android.ui.common.theme.LinkdingTheme
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainActivity : ComponentActivity(), KoinComponent {

    private val settings: Settings by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            LinkdingTheme {
                LinkdingNavHost(
                    settings = settings
                )
            }
        }
    }
}
