package com.example.simplebledetection

import android.util.Log

open class BLEDevice {

    protected var name: String = ""

    /**
     * The measured signal strength of the Bluetooth packet
     */
    protected var rssi: Int = 0
    protected var rawByteData: ByteArray = ByteArray(30)

    private val TAG = "BLEDevice"

    constructor(deviceName: String?, deviceRssi: Int?, byteData: ByteArray?) {
        if (byteData != null) {
            rawByteData = byteData
        }
        if (deviceName != null) {
            name = deviceName
        }
        if (deviceRssi != null) {
            rssi = deviceRssi
        }
    }

    public fun getDeviceName(): String {
        return name
    }

    public fun getDeviceRssi(): Int {
        return rssi
    }

    fun isIBeacon(): Boolean {
//        if (
//            // company ID
//            (rawByteData[5].toInt() and 0xFF) == 0x4C
//            &&
//            ((rawByteData[6].toInt() and 0xFF) == 0x0C)
//            &&
//            // beacon type
//            ((rawByteData[7].toInt() and 0xFF) == 0x02)
//            &&
//            ((rawByteData[8].toInt() and 0xFF) == 0x15)
//        )
//            return true
//        return false

        var startByte = 2
        while (startByte <= 5) {
            if (rawByteData[startByte + 2].toInt() and 0xff == 0x02 && rawByteData[startByte + 3].toInt() and 0xff == 0x15) {
                // debug result: startByte = 5
                return true
            }
            startByte++
        }
        return false
    }


    fun bytesToHex(bytes: ByteArray) : String {
        return rawByteData.joinToString("") {
             String.format("0x%02X", it)
        }
    }

    fun bytesToHex2(bytes: ByteArray) : String {
        return rawByteData.joinToString("") {
            (0xFF and it.toInt()).toString(16).padStart(2, '0')
        }
    }


    private fun bytesToHex3(bytes: ByteArray): String {
        val hexArray = "0123456789ABCDEF".toCharArray()
        val hexChars = CharArray(bytes.size * 2)
        for (j in bytes.indices) {
            val v: Int = bytes[j].toInt() and 0xFF
            hexChars[j * 2] = hexArray.get(v ushr 4)
            hexChars[j * 2 + 1] = hexArray.get(v and 0x0F)
        }
        return String(hexChars)
    }


    fun parseUUID(): String {
        var startByte = 2
        while (startByte <= 5) {
            if (rawByteData[startByte + 2].toInt() and 0xff == 0x02 && rawByteData[startByte + 3].toInt() and 0xff == 0x15) {
                val uuidBytes = ByteArray(16)
                System.arraycopy(rawByteData, startByte + 4, uuidBytes, 0, 16)
                val hexString = bytesToHex(uuidBytes)
                // 0x020x010x1A0x1A0xFF0x4C0x000x020x150xE80xC60x560x020x6D0x9C0x440xEF0x970x340xB20xD30xEF0x1C0xD90x610x000x010x070x210xC50x000x000x000x000x000x000x000x000x000x000x000x000x000x000x000x000x000x000x000x000x000x000x000x000x000x000x000x000x000x000x000x00
                Log.d(TAG, hexString)
                val hexString2 = bytesToHex2(uuidBytes)
                // 02011a1aff4c000215e8c656026d9c44ef9734b2d3ef1cd96100010721c50000000000000000000000000000000000000000000000000000000000000000
                Log.d(TAG, hexString2)
                val hexString3 = bytesToHex3(uuidBytes)
                // E8C656026D9C44EF9734B2D3EF1CD961
                Log.d(TAG, hexString3)
                val sb = StringBuilder()
                if (hexString.isNullOrEmpty()) {
                    hexString?.let {
                        sb.append(it.substring(0, 8))
                        sb.append("-")
                        sb.append(it.substring(8, 12))
                        sb.append("-")
                        sb.append(it.substring(12, 16))
                        sb.append("-")
                        sb.append(it.substring(16, 20))
                        sb.append("-")
                        sb.append(it.substring(20, 32))
                    }

                }
                return sb.toString()
            }
            startByte++
        }
        return ""
    }

}