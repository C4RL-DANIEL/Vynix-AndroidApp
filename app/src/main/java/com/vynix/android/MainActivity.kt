package com.vynix.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.vynix.android.navigation.AppNavGraph
import com.vynix.android.ui.theme.VynixTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VynixTheme {
                AppNavGraph()
            }
        }
    }
}
