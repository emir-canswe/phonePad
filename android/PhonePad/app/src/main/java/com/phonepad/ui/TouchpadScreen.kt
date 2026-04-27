package com.phonepad.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.phonepad.network.WebSocketClient
import com.phonepad.utils.GestureHandler

@Composable
fun TouchpadScreen(navController: NavController, webSocketClient: WebSocketClient) {
    val gestureHandler = remember { GestureHandler(webSocketClient) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Üst Bar - Ayarlar butonu
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = { navController.navigate("settings") }) {
                Icon(Icons.Default.Settings, contentDescription = "Ayarlar")
            }
        }

        // Dokunmatik Alan
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.DarkGray)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        gestureHandler.sendMove(dragAmount.x, dragAmount.y)
                    }
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { gestureHandler.sendClick("left") },
                        onDoubleTap = { gestureHandler.sendDoubleClick() },
                        onLongPress = { gestureHandler.sendClick("right") }
                    )
                }
        ) {
            Text(
                "Dokunmatik Alan",
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Alt Butonlar
        Row(
            modifier = Modifier.fillMaxWidth().height(80.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { gestureHandler.sendClick("left") },
                modifier = Modifier.weight(1f).fillMaxHeight().padding(4.dp)
            ) {
                Text("Sol Tık")
            }
            Button(
                onClick = { gestureHandler.sendClick("right") },
                modifier = Modifier.weight(1f).fillMaxHeight().padding(4.dp)
            ) {
                Text("Sağ Tık")
            }
        }
    }
}
