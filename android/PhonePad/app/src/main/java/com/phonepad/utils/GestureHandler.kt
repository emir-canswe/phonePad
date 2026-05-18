package com.phonepad.utils

import com.phonepad.bluetooth.BluetoothHidManager
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.GlobalScope

class GestureHandler(private val bluetoothManager: BluetoothHidManager) {
    
    // Hassasiyet: 2.5x — daha hızlı fare hareketi
    var sensitivity: Float = 2.5f
    
    private var remainderX = 0f
    private var remainderY = 0f

    fun sendMove(dx: Float, dy: Float) {
        val totalX = (dx * sensitivity) + remainderX
        val totalY = (dy * sensitivity) + remainderY
        
        val scaledDx = totalX.toInt()
        val scaledDy = totalY.toInt()
        
        // Kalan ondalık değeri birik — hiçbir küçük hareket kaybolmasın
        remainderX = totalX - scaledDx
        remainderY = totalY - scaledDy

        // Sıfır kontrolü yok → her küçük hareketi anında ilet
        bluetoothManager.sendMouseMovement(dx = scaledDx, dy = scaledDy)
    }

    fun sendClick(button: String = "left") {
        val left = button == "left"
        val right = button == "right"
        bluetoothManager.sendMouseMovement(dx = 0, dy = 0, leftButton = left, rightButton = right)
        bluetoothManager.sendMouseMovement(dx = 0, dy = 0, leftButton = false, rightButton = false)
    }

    fun sendDoubleClick() {
        kotlinx.coroutines.GlobalScope.launch {
            sendClick("left")
            kotlinx.coroutines.delay(50)
            sendClick("left")
        }
    }

    fun sendScroll(dy: Float) {
        val scrollVal = (dy / 10).toInt()
        if (scrollVal != 0) {
            bluetoothManager.sendMouseMovement(dx = 0, dy = 0, wheel = scrollVal)
        }
    }
    
    fun sendDrag(dx: Float, dy: Float) {
        val totalX = (dx * sensitivity) + remainderX
        val totalY = (dy * sensitivity) + remainderY
        
        val scaledDx = totalX.toInt()
        val scaledDy = totalY.toInt()
        
        remainderX = totalX - scaledDx
        remainderY = totalY - scaledDy

        bluetoothManager.sendMouseMovement(dx = scaledDx, dy = scaledDy, leftButton = true)
    }
    
    fun releaseDrag() {
        bluetoothManager.sendMouseMovement(dx = 0, dy = 0, leftButton = false)
    }
}
