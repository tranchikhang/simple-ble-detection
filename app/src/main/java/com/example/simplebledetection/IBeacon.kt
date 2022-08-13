package com.example.simplebledetection

class IBeacon(deviceName: String?, deviceRssi: Int?, byteData: ByteArray) : BLEDevice(deviceName, deviceRssi,
    byteData
) {
    private lateinit var uuid: String
    private var major: Int = -1
    private var minor: Int = -1

}