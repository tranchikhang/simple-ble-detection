package com.example.simplebledetection

import android.util.Log

class IBeacon {

    /**
     * beacon UUID
     */
    private var uuid: String = ""

    /**
     * The measured signal strength of the Bluetooth packet
     */
    protected var rssi: Int = 0

    /**
     * packet raw data
     */
    protected var rawByteData: ByteArray = ByteArray(30)

    /**
     * major minor, and their position (based on iBeacon specs)
     */
    private var major: Int = -1
    protected val majorPosStart = 25
    protected val majorPosEnd = 26

    private var minor: Int = -1
    protected val minorPosStart = 27
    protected val minorPosEnd = 28

    private val TAG = "IBeacon"

    constructor(packetData: ByteArray) {
        rawByteData = packetData
    }

    private fun parseUUID() {
        var startByte = 2
        while (startByte <= 5) {
            if (rawByteData[startByte + 2].toInt() and 0xff == 0x02 && rawByteData[startByte + 3].toInt() and 0xff == 0x15) {
                val uuidBytes = ByteArray(16)
                System.arraycopy(rawByteData, startByte + 4, uuidBytes, 0, 16)
                val hexString = BLEDevice.bytesToHex(uuidBytes)
                // 0x020x010x1A0x1A0xFF0x4C0x000x020x150xE80xC60x560x020x6D0x9C0x440xEF0x970x340xB20xD30xEF0x1C0xD90x610x000x010x070x210xC50x000x000x000x000x000x000x000x000x000x000x000x000x000x000x000x000x000x000x000x000x000x000x000x000x000x000x000x000x000x000x000x00
                Log.d(TAG, hexString)
                val sb = StringBuilder()
                if (!hexString.isNullOrEmpty()) {
                    uuid = hexString.substring(0, 8) + "-" +
                            hexString.substring(8, 12) + "-" +
                            hexString.substring(12, 16) + "-" +
                            hexString.substring(16, 20) + "-" +
                            hexString.substring(20, 32)
                }
            }
            startByte++
        }
    }

    fun getUUID(): String {
        if (uuid.isNullOrEmpty()) {
            parseUUID();
        }
        return uuid
    }

    fun getMajor(): Int {
        return (rawByteData[majorPosStart].toInt() and 0xff) * 0x100 + (rawByteData[majorPosEnd].toInt() and 0xff)
    }

    fun getMinor(): Int {
        return (rawByteData[minorPosStart].toInt() and 0xff) * 0x100 + (rawByteData[minorPosEnd].toInt() and 0xff)
    }

}