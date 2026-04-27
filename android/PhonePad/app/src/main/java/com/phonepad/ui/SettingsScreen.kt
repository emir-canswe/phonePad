package com.phonepad.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SettingsScreen(navController: NavController) {
    var sensitivity by remember { mutableStateOf(1.5f) }
    var scrollSpeed by remember { mutableStateOf(1.0f) }
    var vibrationEnabled by remember { mutableStateOf(true) }
    var wakeLockEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("Ayarlar", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        Text("Fare Hassasiyeti: ${"%.1f".format(sensitivity)}x")
        Slider(
            value = sensitivity,
            onValueChange = { sensitivity = it },
            valueRange = 0.5f..3.0f
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Scroll Hızı: ${"%.1f".format(scrollSpeed)}x")
        Slider(
            value = scrollSpeed,
            onValueChange = { scrollSpeed = it },
            valueRange = 0.5f..3.0f
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Titreşim Geri Bildirimi")
            Switch(
                checked = vibrationEnabled,
                onCheckedChange = { vibrationEnabled = it }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Ekranı Açık Tut (WakeLock)")
            Switch(
                checked = wakeLockEnabled,
                onCheckedChange = { wakeLockEnabled = it }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Geri Dön")
        }
    }
}
