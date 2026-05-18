package com.phonepad.ui

import androidx.compose.foundation.background
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.phonepad.bluetooth.BluetoothHidManager
import com.phonepad.utils.GestureHandler

@Composable
fun TouchpadScreen(navController: NavController, bluetoothHidManager: BluetoothHidManager) {
    val gestureHandler = remember { GestureHandler(bluetoothHidManager) }
    val connectionStatus by bluetoothHidManager.connectionStatus.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF0F0F13))) {
        // Üst Bar - Ayarlar butonu ve Durum
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🖱️", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (connectionStatus) "Bağlı" else "Bağlantı Koptu",
                    color = if (connectionStatus) Color(0xFF00E676) else Color.Red,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
            IconButton(onClick = { navController.navigate("settings") }) {
                Icon(Icons.Default.Settings, contentDescription = "Ayarlar", tint = Color.Gray)
            }
        }

        // Dokunmatik Alan — ham pointer ile sıfır gecikme
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFF1A1A20))
                .pointerInput(Unit) {
                    // Ham pointer: Android'in iç filtresi yok → anında tepki
                    awaitPointerEventScope {
                        var lastX = 0f
                        var lastY = 0f
                        var isDown = false
                        while (true) {
                            val event = awaitPointerEvent()
                            val pointer = event.changes.firstOrNull() ?: continue
                            when {
                                pointer.pressed && !isDown -> {
                                    // Parmak indi
                                    isDown = true
                                    lastX = pointer.position.x
                                    lastY = pointer.position.y
                                    pointer.consume()
                                }
                                pointer.pressed && isDown -> {
                                    // Parmak sürükleniyor
                                    val dx = pointer.position.x - lastX
                                    val dy = pointer.position.y - lastY
                                    lastX = pointer.position.x
                                    lastY = pointer.position.y
                                    gestureHandler.sendMove(dx, dy)
                                    pointer.consume()
                                }
                                !pointer.pressed && isDown -> {
                                    // Parmak kalktı — tek tık mı sürükleme mi?
                                    val totalMove = Math.abs(pointer.position.x - lastX) + Math.abs(pointer.position.y - lastY)
                                    if (totalMove < 5f) {
                                        gestureHandler.sendClick("left")
                                    }
                                    gestureHandler.releaseDrag()
                                    isDown = false
                                    pointer.consume()
                                }
                            }
                        }
                    }
                }
        ) {
            Text(
                "Touchpad",
                color = Color.DarkGray,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Alt Butonlar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(80.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { gestureHandler.sendClick("left") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C35)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.weight(1f).fillMaxHeight()
            ) {
                Text("Sol Tık", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = { gestureHandler.sendClick("right") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C2C35)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.weight(1f).fillMaxHeight()
            ) {
                Text("Sağ Tık", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
