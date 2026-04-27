package com.phonepad.ui

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.phonepad.network.WebSocketClient

@Composable
fun ConnectScreen(navController: NavController, webSocketClient: WebSocketClient) {
    val context = LocalContext.current
    val sharedPrefs = context.getSharedPreferences("PhonePadPrefs", Context.MODE_PRIVATE)
    
    var ipAddress by remember { mutableStateOf(sharedPrefs.getString("ip", "192.168.1.") ?: "") }
    var port by remember { mutableStateOf("8765") }
    
    val connectionStatus by webSocketClient.connectionStatus.collectAsState()

    LaunchedEffect(connectionStatus) {
        if (connectionStatus) {
            navController.navigate("touchpad") {
                popUpTo("connect") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("PhonePad'e Hoş Geldiniz", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))
        
        OutlinedTextField(
            value = ipAddress,
            onValueChange = { ipAddress = it },
            label = { Text("IP Adresi") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = port,
            onValueChange = { port = it },
            label = { Text("Port") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = {
                sharedPrefs.edit().putString("ip", ipAddress).apply()
                webSocketClient.connect(ipAddress, port)
            },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Bağlan")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Text(if (connectionStatus) "Bağlı" else "Bağlantı Bekleniyor...")
    }
}
