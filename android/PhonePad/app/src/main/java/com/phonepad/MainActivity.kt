package com.phonepad

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.phonepad.network.WebSocketClient
import com.phonepad.ui.ConnectScreen
import com.phonepad.ui.SettingsScreen
import com.phonepad.ui.TouchpadScreen

class MainActivity : ComponentActivity() {
    private val webSocketClient = WebSocketClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // WakeLock support via Window flag
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    
                    NavHost(navController = navController, startDestination = "connect") {
                        composable("connect") {
                            ConnectScreen(
                                navController = navController,
                                webSocketClient = webSocketClient
                            )
                        }
                        composable("touchpad") {
                            TouchpadScreen(
                                navController = navController,
                                webSocketClient = webSocketClient
                            )
                        }
                        composable("settings") {
                            SettingsScreen(
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocketClient.disconnect()
    }
}
