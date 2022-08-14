package com.example.simplebledetection

import android.util.Log

object BLEDevice {
     fun isIBeacon(packetData: ByteArray): Boolean {

        var startByte = 2
        while (startByte <= 5) {
            if (packetData[startByte + 2].toInt() and 0xff == 0x02 && packetData[startByte + 3].toInt() and 0xff == 0x15) {
                // debug result: startByte = 5
                return true
            }
            startByte++
        }
        return false
    }

    fun bytesToHex(bytes: ByteArray): String {
        val hexArray = "0123456789ABCDEF".toCharArray()
        val hexChars = CharArray(bytes.size * 2)
        for (j in bytes.indices) {
            val v: Int = bytes[j].toInt() and 0xFF
            hexChars[j * 2] = hexArray.get(v ushr 4)
            hexChars[j * 2 + 1] = hexArray.get(v and 0x0F)
        }
        return String(hexChars)
    }
}