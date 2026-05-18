package com.phonepad.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.phonepad.bluetooth.BluetoothHidManager

@SuppressLint("MissingPermission")
@Composable
fun ConnectScreen(navController: NavController, bluetoothHidManager: BluetoothHidManager) {
    val connectionStatus by bluetoothHidManager.connectionStatus.collectAsState()
    val isScanning by bluetoothHidManager.isScanning.collectAsState()
    val discoveredDevices by bluetoothHidManager.discoveredDevices.collectAsState()
    val pairedDevices = remember { bluetoothHidManager.getPairedDevices() }
    
    LaunchedEffect(connectionStatus) {
        if (connectionStatus) {
            navController.navigate("touchpad") {
                popUpTo("connect") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F13)) // Koyu arayüz arka planı
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text("🔵", fontSize = 48.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "PhonePad",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            color = Color.White
        )
        Text(
            text = "Cihazınız bağlanmaya hazır",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        val context = androidx.compose.ui.platform.LocalContext.current
        Button(
            onClick = { 
                val discoverableIntent = android.content.Intent(android.bluetooth.BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
                    putExtra(android.bluetooth.BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
                }
                context.startActivity(discoverableIntent)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().height(60.dp)
        ) {
            Text("Bilgisayardan Bağlan", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { bluetoothHidManager.startDiscovery() },
            enabled = !isScanning,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1E1E24), 
                disabledContainerColor = Color(0xFF1E1E24)
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().height(60.dp)
        ) {
            Text(if (isScanning) "Ağ Aranıyor..." else "Ağ Ara (Cihaz Bul)", color = Color.White, fontSize = 16.sp)
        }
        
        Spacer(modifier = Modifier.height(24.dp))

        if (isScanning) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = Color(0xFF00E676), trackColor = Color(0xFF1E1E24))
            Spacer(modifier = Modifier.height(16.dp))
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            if (pairedDevices.isNotEmpty()) {
                item {
                    Text("Kayıtlı Cihazlar", style = MaterialTheme.typography.titleMedium, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(pairedDevices) { device ->
                    DeviceItemCard(name = device.name ?: "Bilinmeyen Cihaz", address = device.address) {
                        bluetoothHidManager.connectToDevice(device)
                    }
                }
            }

            if (discoveredDevices.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Bulunan Yeni Cihazlar", style = MaterialTheme.typography.titleMedium, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(discoveredDevices) { device ->
                    DeviceItemCard(name = device.name ?: "Bilinmeyen Cihaz", address = device.address) {
                        bluetoothHidManager.connectToDevice(device)
                    }
                }
            }
        }
    }
}

@Composable
fun DeviceItemCard(name: String, address: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A20))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(name, style = MaterialTheme.typography.bodyLarge, color = Color.White, fontWeight = FontWeight.SemiBold)
            Text(address, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
    }
}
