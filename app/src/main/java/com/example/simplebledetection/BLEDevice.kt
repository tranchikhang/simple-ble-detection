package com.example.simplebledetection

import android.bluetooth.le.ScanResult

open class BLEDevice(scanResult: ScanResult) {

    /**
     * The measured signal strength of the Bluetooth packet
     */
    private var rssi: Int = 0

    /**
     * Device mac address
     */
    private var address: String = ""

    /**
     * Device friendly name
     */
    private var name: String = ""


    init {
        if (scanResult.device.name != null) {
            name = scanResult.device.name
        }
        address = scanResult.device.address
        rssi = scanResult.rssi
    }

    fun getAddress(): String {
        return address
    }

    fun getRssi(): Int {
        return rssi
    }
}