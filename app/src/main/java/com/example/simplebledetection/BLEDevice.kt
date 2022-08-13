package com.example.simplebledetection

open class BLEDevice {

    protected var name: String = ""

    /**
     * The measured signal strength of the Bluetooth packet
     */
    protected var rssi: Int = 0
    protected var rawByteData: ByteArray = ByteArray(30)

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

}